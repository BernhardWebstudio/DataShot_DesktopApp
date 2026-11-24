/**
 * TestExternalProcessRunner.java
 * edu.harvard.mcz.imagecapture.tests
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
 */
package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner.ProcessResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for {@link ExternalProcessRunner}.
 */
public class TestExternalProcessRunner {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@After
	public void tearDown() {
		ExternalProcessRunner.shared().clearCache();
	}

	@Test
	public void testTimeoutEnforcementDestroysLongRunningProcess() throws Exception {
		ExternalProcessRunner runner = new ExternalProcessRunner();
		ProcessResult result = runner.run(List.of("/bin/sh", "-c", "sleep 5"), 150L);
		assertTrue("Expected timedOut == true", result.timedOut());
		assertFalse("Expected isSuccess == false", result.isSuccess());
		assertEquals(-1, result.exitCode());
	}

	@Test
	public void testLargeConcurrentStdoutAndStderrDrainingWithoutDeadlock() throws Exception {
		ExternalProcessRunner runner = new ExternalProcessRunner();
		ProcessResult result = runner
				.run(List
						.of("/bin/sh", "-c",
								"i=0; while [ $i -lt 1000 ]; do printf 'OUT_123456789012345678901234567890\\n'; "
										+ "printf 'ERR_123456789012345678901234567890\\n' >&2; i=$((i+1)); done"),
						10000L);
		assertTrue(result.isSuccess());
		assertFalse(result.timedOut());
		assertTrue(result.stdout().length() > 30000);
		assertTrue(result.stderr().length() > 30000);
	}

	@Test
	public void testExecutableAvailabilityAndPathCachingWithCounterScript() throws Exception {
		Path counterFile = tempFolder.getRoot().toPath().resolve("probe_counter.txt");
		Path script = tempFolder.getRoot().toPath().resolve("probe_script.sh");
		String body = "#!/bin/sh\nprintf 'hit\\n' >> '" + counterFile.toAbsolutePath() + "'\nexit 0\n";
		Files.writeString(script, body, StandardCharsets.UTF_8);
		assertTrue(script.toFile().setExecutable(true));

		ExternalProcessRunner runner = new ExternalProcessRunner();
		String cmd = script.toAbsolutePath().toString();

		assertTrue(runner.isCommandAvailable(cmd, 2000L, "--version"));
		assertTrue(runner.isCommandAvailable(cmd, 2000L, "--version"));
		assertEquals(1, Files.readAllLines(counterFile, StandardCharsets.UTF_8).size());

		runner.clearCache();
		Optional<String> resolved1 = runner.resolveFirstAvailable("tool-key", List.of("/nonexistent/missing_tool", cmd),
				2000L, "--version");
		Optional<String> resolved2 = runner.resolveFirstAvailable("tool-key", List.of("/nonexistent/missing_tool", cmd),
				2000L, "--version");
		assertEquals(Optional.of(cmd), resolved1);
		assertEquals(Optional.of(cmd), resolved2);
		assertEquals(2, Files.readAllLines(counterFile, StandardCharsets.UTF_8).size());

		runner.clearCache();
		assertTrue(runner.isCommandAvailable(cmd, 2000L, "--version"));
		assertEquals(3, Files.readAllLines(counterFile, StandardCharsets.UTF_8).size());
	}

	@Test
	public void testWithTempFileDeletesEvenOnIOException() {
		final Path[] captured = new Path[1];
		try {
			ExternalProcessRunner.withTempFile("epr_test_", ".tmp", path -> {
				captured[0] = path;
				assertTrue(Files.exists(path));
				throw new IOException("simulated failure");
			});
			fail("Expected IOException");
		} catch (IOException expected) {
			assertNotNull(captured[0]);
			assertFalse("Temp file must be deleted on IOException", Files.exists(captured[0]));
		}
	}
}
