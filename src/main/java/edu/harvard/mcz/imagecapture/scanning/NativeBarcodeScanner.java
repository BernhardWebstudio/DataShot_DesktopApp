/**
 * NativeBarcodeScanner.java
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

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner.ProcessResult;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BarcodeScanner} backed by the optional macOS/Windows native QR helper
 * executable, using {@link ExternalProcessRunner} with cached helper lookup.
 */
public class NativeBarcodeScanner implements BarcodeScanner {

	private static final Logger log = LoggerFactory.getLogger(NativeBarcodeScanner.class);
	private static final long DEFAULT_TIMEOUT_MS = 2500L;
	private static final long PROBE_TIMEOUT_MS = 1500L;

	private final ExternalProcessRunner processRunner;
	private final boolean enabled;
	private final long timeoutMs;
	private final String macosExecutable;
	private final String windowsExecutable;

	/**
	 * Creates a {@link NativeBarcodeScanner} configured from {@link Singleton}
	 * properties.
	 */
	public NativeBarcodeScanner() {
		this(ExternalProcessRunner.shared(), readSingletonProperties());
	}

	/**
	 * Creates a {@link NativeBarcodeScanner} configured from the given properties.
	 *
	 * @param processRunner
	 *            runner used to execute and cache external commands
	 * @param properties
	 *            application properties
	 */
	public NativeBarcodeScanner(ExternalProcessRunner processRunner, Properties properties) {
		this(processRunner,
				Boolean.parseBoolean(getProp(properties, ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "true").trim()),
				parseLong(getProp(properties, ImageCaptureProperties.KEY_QR_NATIVE_TIMEOUT_MS, "2500"),
						DEFAULT_TIMEOUT_MS),
				getProp(properties, ImageCaptureProperties.KEY_QR_NATIVE_MACOS_EXECUTABLE, ""),
				getProp(properties, ImageCaptureProperties.KEY_QR_NATIVE_WINDOWS_EXECUTABLE, ""));
	}

	/**
	 * Creates a {@link NativeBarcodeScanner} with explicit configuration.
	 *
	 * @param processRunner
	 *            process runner
	 * @param enabled
	 *            whether the native scanner is enabled
	 * @param timeoutMs
	 *            invocation timeout in milliseconds
	 * @param macosExecutable
	 *            optional configured macOS helper path
	 * @param windowsExecutable
	 *            optional configured Windows helper path
	 */
	public NativeBarcodeScanner(ExternalProcessRunner processRunner, boolean enabled, long timeoutMs,
			String macosExecutable, String windowsExecutable) {
		this.processRunner = Objects.requireNonNull(processRunner, "processRunner must not be null");
		this.enabled = enabled;
		this.timeoutMs = timeoutMs > 0 ? timeoutMs : DEFAULT_TIMEOUT_MS;
		this.macosExecutable = macosExecutable == null ? "" : macosExecutable.trim();
		this.windowsExecutable = windowsExecutable == null ? "" : windowsExecutable.trim();
	}

	@Override
	public String id() {
		return "native";
	}

	@Override
	public boolean isAvailable() {
		return enabled && resolveHelperPath().isPresent();
	}

	@Override
	public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
		if (image == null || !enabled) {
			return List.of();
		}
		Optional<String> helperPath = resolveHelperPath();
		if (helperPath.isEmpty()) {
			return List.of();
		}
		ScanOptions effectiveOptions = options == null ? ScanOptions.defaults() : options;
		try {
			return ExternalProcessRunner.withTempFile("qr_native_", ".png", tempImagePath -> {
				ImageIO.write(image, "png", tempImagePath.toFile());
				List<String> command = buildCommand(helperPath.get(), tempImagePath,
						effectiveOptions.preferredRegion().orElse(null));
				ProcessResult result = processRunner.run(command, timeoutMs, true);
				if (result.timedOut()) {
					log.debug("Native barcode scanner timed out after {}ms", timeoutMs);
					return List.of();
				}
				if (result.exitCode() != 0) {
					log.debug("Native barcode scanner exited with {}: {}", result.exitCode(), result.stdout());
					return List.of();
				}
				String payload = ExternalScannerSupport.extractJsonPayload(result.stdout());
				return ExternalScannerSupport.parseQrDetectionsFromJson(payload, id());
			});
		} catch (IOException e) {
			log.debug("Native barcode scanner failed: {}", e.getMessage());
			return List.of();
		}
	}

	private List<String> buildCommand(String executable, Path tempImagePath, Rectangle preferredRegion) {
		List<String> command = new ArrayList<>();
		command.add(executable);
		command.add(tempImagePath.toString());
		if (preferredRegion != null) {
			command.add("--preferred-left");
			command.add(Integer.toString(preferredRegion.x));
			command.add("--preferred-top");
			command.add(Integer.toString(preferredRegion.y));
			command.add("--preferred-width");
			command.add(Integer.toString(preferredRegion.width));
			command.add("--preferred-height");
			command.add(Integer.toString(preferredRegion.height));
		}
		return command;
	}

	private Optional<String> resolveHelperPath() {
		String os = ExternalScannerSupport.getOsKey();
		String configured = "";
		if ("macos".equals(os)) {
			configured = macosExecutable;
		} else if ("windows".equals(os)) {
			configured = windowsExecutable;
		}

		if (!configured.isEmpty()) {
			Path configuredPath = Paths.get(configured);
			if (Files.isExecutable(configuredPath)
					|| processRunner.isCommandAvailable(configured, PROBE_TIMEOUT_MS, "--version")) {
				return Optional.of(configured);
			}
		}

		List<String> candidates = new ArrayList<>();
		for (String archSuffix : getArchSuffixes()) {
			candidates.add("native/bin/qr-native-" + os + archSuffix);
			candidates.add("native/bin/qr-native-" + os + archSuffix + ".exe");
			candidates.add("qr-native-" + os + archSuffix);
			candidates.add("qr-native-" + os + archSuffix + ".exe");
		}
		for (String candidate : candidates) {
			Path candidatePath = Paths.get(candidate);
			if (Files.exists(candidatePath)) {
				return Optional.of(candidatePath.toString());
			}
		}
		return processRunner.resolveFirstAvailable("native-qr:" + os, candidates, PROBE_TIMEOUT_MS, "--version");
	}

	private static String[] getArchSuffixes() {
		String arch = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
		if (arch.contains("arm64") || arch.contains("aarch64")) {
			return new String[]{"-arm64", "-aarch64", "-x64", "-amd64", ""};
		}
		return new String[]{"-x64", "-amd64", "-arm64", "-aarch64", ""};
	}

	private static Properties readSingletonProperties() {
		try {
			return Singleton.getSingletonInstance().getProperties().getProperties();
		} catch (Exception e) {
			return new Properties();
		}
	}

	private static String getProp(Properties properties, String key, String defaultValue) {
		if (properties == null) {
			return defaultValue;
		}
		String val = properties.getProperty(key);
		return val == null ? defaultValue : val;
	}

	private static long parseLong(String value, long defaultValue) {
		try {
			return Long.parseLong(value.trim());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
