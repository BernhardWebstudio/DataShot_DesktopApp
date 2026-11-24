/**
 * ExternalProcessRunner.java
 * edu.harvard.mcz.imagecapture.scanning
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.scanning;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe runner for external executables using {@link ProcessBuilder} with
 * concurrent stdout/stderr draining, timeout enforcement, executable
 * availability caching, and safe temp-file lifecycle helpers.
 */
public class ExternalProcessRunner {

	private static final Logger log = LoggerFactory.getLogger(ExternalProcessRunner.class);
	private static final ExternalProcessRunner SHARED = new ExternalProcessRunner();
	private static final long DRAIN_JOIN_TIMEOUT_MS = 1000L;

	private final ConcurrentHashMap<String, Boolean> availabilityCache = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Optional<String>> resolvedCommandCache = new ConcurrentHashMap<>();

	/**
	 * Immutable result of an external process execution.
	 *
	 * @param exitCode
	 *            process exit code ({@code -1} if timed out before exiting)
	 * @param stdout
	 *            captured UTF-8 standard output
	 * @param stderr
	 *            captured UTF-8 standard error (empty when streams are merged)
	 * @param timedOut
	 *            {@code true} if the process exceeded the configured timeout
	 */
	public record ProcessResult(int exitCode, String stdout, String stderr, boolean timedOut) {

		public ProcessResult {
			stdout = stdout == null ? "" : stdout;
			stderr = stderr == null ? "" : stderr;
		}

		/**
		 * @return {@code true} when the process completed without timing out and exited
		 *         with code {@code 0}
		 */
		public boolean isSuccess() {
			return !timedOut && exitCode == 0;
		}
	}

	/**
	 * Callback interface for operations scoped to a temporary file that is
	 * guaranteed to be deleted afterward.
	 *
	 * @param <T>
	 *            return type
	 */
	@FunctionalInterface
	public interface TempFileAction<T> {
		T execute(Path tempFile) throws IOException;
	}

	/**
	 * @return shared process-wide runner with executable caching
	 */
	public static ExternalProcessRunner shared() {
		return SHARED;
	}

	/**
	 * Executes a command with separate stdout/stderr draining on daemon threads
	 * prior to waiting with a timeout.
	 *
	 * @param command
	 *            explicit argument vector
	 * @param timeoutMs
	 *            timeout in milliseconds
	 * @return immutable {@link ProcessResult}
	 * @throws IOException
	 *             if the process cannot be started
	 */
	public ProcessResult run(List<String> command, long timeoutMs) throws IOException {
		return run(command, timeoutMs, false);
	}

	/**
	 * Executes a command with concurrent daemon stream draining before
	 * {@link Process#waitFor(long, TimeUnit)}.
	 *
	 * @param command
	 *            explicit argument vector
	 * @param timeoutMs
	 *            timeout in milliseconds
	 * @param mergeErrorStream
	 *            whether stderr should be merged into stdout
	 * @return immutable {@link ProcessResult}
	 * @throws IOException
	 *             if the process cannot be started
	 */
	public ProcessResult run(List<String> command, long timeoutMs, boolean mergeErrorStream) throws IOException {
		Objects.requireNonNull(command, "command must not be null");
		if (command.isEmpty()) {
			throw new IllegalArgumentException("command must not be empty");
		}
		long effectiveTimeoutMs = Math.max(1L, timeoutMs);
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(mergeErrorStream);
		Process process = pb.start();

		ByteArrayOutputStream stdoutBuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream stderrBuffer = new ByteArrayOutputStream();

		Thread stdoutThread = startDrainThread(process.getInputStream(), stdoutBuffer, "proc-stdout-drain");
		Thread stderrThread = mergeErrorStream
				? null
				: startDrainThread(process.getErrorStream(), stderrBuffer, "proc-stderr-drain");

		boolean timedOut = false;
		int exitCode = -1;
		try {
			boolean finished = process.waitFor(effectiveTimeoutMs, TimeUnit.MILLISECONDS);
			if (!finished) {
				timedOut = true;
				process.destroyForcibly();
				process.waitFor(500L, TimeUnit.MILLISECONDS);
			} else {
				exitCode = process.exitValue();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			process.destroyForcibly();
			timedOut = true;
		} finally {
			joinQuietly(stdoutThread);
			if (stderrThread != null) {
				joinQuietly(stderrThread);
			}
		}

		String stdout = stdoutBuffer.toString(StandardCharsets.UTF_8);
		String stderr = stderrBuffer.toString(StandardCharsets.UTF_8);
		return new ProcessResult(exitCode, stdout, stderr, timedOut);
	}

	/**
	 * Checks and caches whether an executable responds with exit code {@code 0}
	 * when invoked with the given probe arguments (defaulting to
	 * {@code ["--version"]}).
	 *
	 * @param executable
	 *            command name or path
	 * @param timeoutMs
	 *            timeout in milliseconds
	 * @param probeArgs
	 *            optional arguments (defaults to {@code --version})
	 * @return {@code true} if the executable launched and exited with {@code 0}
	 */
	public boolean isCommandAvailable(String executable, long timeoutMs, String... probeArgs) {
		if (executable == null || executable.isBlank()) {
			return false;
		}
		String trimmed = executable.trim();
		String[] args = (probeArgs == null || probeArgs.length == 0) ? new String[]{"--version"} : probeArgs;
		String cacheKey = trimmed + "|" + String.join("\0", args);
		return availabilityCache.computeIfAbsent(cacheKey, key -> probeCommand(trimmed, timeoutMs, args));
	}

	/**
	 * Resolves and caches the first available candidate executable under the given
	 * logical cache key.
	 *
	 * @param cacheKey
	 *            key identifying the logical lookup
	 * @param candidates
	 *            ordered candidate commands or paths
	 * @param timeoutMs
	 *            timeout per probe in milliseconds
	 * @param probeArgs
	 *            probe arguments (defaults to {@code --version})
	 * @return first available command, or {@link Optional#empty()}
	 */
	public Optional<String> resolveFirstAvailable(String cacheKey, List<String> candidates, long timeoutMs,
			String... probeArgs) {
		Objects.requireNonNull(cacheKey, "cacheKey must not be null");
		if (candidates == null || candidates.isEmpty()) {
			return Optional.empty();
		}
		return resolvedCommandCache.computeIfAbsent(cacheKey, key -> {
			for (String candidate : candidates) {
				if (candidate != null && !candidate.isBlank()
						&& isCommandAvailable(candidate.trim(), timeoutMs, probeArgs)) {
					return Optional.of(candidate.trim());
				}
			}
			return Optional.empty();
		});
	}

	/**
	 * Clears cached executable availability and resolved command paths.
	 */
	public void clearCache() {
		availabilityCache.clear();
		resolvedCommandCache.clear();
	}

	/**
	 * Creates a temporary file, executes {@code action}, and guarantees deletion in
	 * a {@code finally} block.
	 *
	 * @param prefix
	 *            temp file prefix
	 * @param suffix
	 *            temp file suffix
	 * @param action
	 *            callback receiving the temp path
	 * @param <T>
	 *            return type
	 * @return action result
	 * @throws IOException
	 *             if temp file creation or action fails
	 */
	public static <T> T withTempFile(String prefix, String suffix, TempFileAction<T> action) throws IOException {
		Objects.requireNonNull(action, "action must not be null");
		Path tempFile = Files.createTempFile(prefix, suffix);
		try {
			return action.execute(tempFile);
		} finally {
			deleteQuietly(tempFile);
		}
	}

	/**
	 * Deletes a path if non-null, logging at trace level on failure.
	 *
	 * @param path
	 *            path to delete
	 */
	public static void deleteQuietly(Path path) {
		if (path != null) {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				log.trace("Failed to remove temporary file {}: {}", path, e.getMessage());
			}
		}
	}

	private boolean probeCommand(String executable, long timeoutMs, String[] args) {
		List<String> command = new ArrayList<>(1 + args.length);
		command.add(executable);
		for (String arg : args) {
			command.add(arg);
		}
		try {
			ProcessResult result = run(command, timeoutMs, true);
			return result.isSuccess();
		} catch (IOException e) {
			return false;
		}
	}

	private Thread startDrainThread(InputStream stream, ByteArrayOutputStream target, String threadName) {
		Thread thread = new Thread(() -> {
			byte[] buffer = new byte[4096];
			try {
				int read;
				while ((read = stream.read(buffer)) != -1) {
					target.write(buffer, 0, read);
				}
			} catch (IOException e) {
				log.trace("Stream drain ended for {}: {}", threadName, e.getMessage());
			}
		}, threadName);
		thread.setDaemon(true);
		thread.start();
		return thread;
	}

	private void joinQuietly(Thread thread) {
		if (thread == null) {
			return;
		}
		try {
			thread.join(DRAIN_JOIN_TIMEOUT_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
