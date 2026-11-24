/**
 * ChromiumBarcodeScanner.java
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Optional {@link BarcodeScanner} backed by headless Chromium's
 * {@code BarcodeDetector} API via {@code qrscan_chromium.html}. Disabled by
 * default unless {@link ImageCaptureProperties#KEY_QR_CHROMIUM_ENABLED} is
 * {@code true}.
 */
public class ChromiumBarcodeScanner implements BarcodeScanner {

	private static final Logger log = LoggerFactory.getLogger(ChromiumBarcodeScanner.class);
	private static final long DEFAULT_TIMEOUT_MS = 20000L;
	private static final long PROBE_TIMEOUT_MS = 1500L;

	private final ExternalProcessRunner processRunner;
	private final boolean enabled;
	private final long timeoutMs;
	private final String configuredExecutable;

	/**
	 * Creates a {@link ChromiumBarcodeScanner} configured from {@link Singleton}
	 * properties.
	 */
	public ChromiumBarcodeScanner() {
		this(ExternalProcessRunner.shared(), readSingletonProperties());
	}

	/**
	 * Creates a {@link ChromiumBarcodeScanner} configured from {@code properties}.
	 *
	 * @param processRunner
	 *            external process runner
	 * @param properties
	 *            configuration properties
	 */
	public ChromiumBarcodeScanner(ExternalProcessRunner processRunner, Properties properties) {
		this(processRunner,
				Boolean.parseBoolean(
						getProp(properties, ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false").trim()),
				parseLong(getProp(properties, ImageCaptureProperties.KEY_QR_CHROMIUM_TIMEOUT_MS, "20000"),
						DEFAULT_TIMEOUT_MS),
				getProp(properties, ImageCaptureProperties.KEY_CHROMIUM_EXECUTABLE, ""));
	}

	/**
	 * Creates a {@link ChromiumBarcodeScanner} with explicit settings.
	 *
	 * @param processRunner
	 *            external process runner
	 * @param enabled
	 *            whether Chromium scanning is enabled
	 * @param timeoutMs
	 *            timeout in milliseconds
	 * @param configuredExecutable
	 *            optional explicit Chromium binary path
	 */
	public ChromiumBarcodeScanner(ExternalProcessRunner processRunner, boolean enabled, long timeoutMs,
			String configuredExecutable) {
		this.processRunner = Objects.requireNonNull(processRunner, "processRunner must not be null");
		this.enabled = enabled;
		this.timeoutMs = timeoutMs > 0 ? timeoutMs : DEFAULT_TIMEOUT_MS;
		this.configuredExecutable = configuredExecutable == null ? "" : configuredExecutable.trim();
	}

	@Override
	public String id() {
		return "chromium";
	}

	@Override
	public boolean isAvailable() {
		return enabled && resolveChromiumExecutable().isPresent() && findScanPage().isPresent();
	}

	@Override
	public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
		if (image == null || !enabled) {
			return List.of();
		}
		Optional<String> chromium = resolveChromiumExecutable();
		Optional<String> scanPage = findScanPage();
		if (chromium.isEmpty() || scanPage.isEmpty()) {
			return List.of();
		}

		try {
			return ExternalProcessRunner.withTempFile("qr_chromium_", ".png", tempImagePath -> {
				ImageIO.write(image, "png", tempImagePath.toFile());
				String imageUri = tempImagePath.toUri().toString();
				String pageUri = Paths.get(scanPage.get()).toUri().toString() + "?image="
						+ URLEncoder.encode(imageUri, StandardCharsets.UTF_8);

				List<String> command = List.of(chromium.get(), "--headless", "--disable-gpu",
						"--allow-file-access-from-files", "--virtual-time-budget=" + timeoutMs, "--dump-dom", pageUri);

				ProcessResult result = processRunner.run(command, timeoutMs + 1000L, true);
				if (result.timedOut() || result.exitCode() != 0) {
					log.debug("Chromium scanner failed (timedOut={}, exit={})", result.timedOut(), result.exitCode());
					return List.of();
				}
				String jsonPayload = extractChromiumJsonPayload(result.stdout());
				return ExternalScannerSupport.parseQrDetectionsFromJson(jsonPayload, id());
			});
		} catch (IOException e) {
			log.debug("Chromium barcode scanner failed: {}", e.getMessage());
			return List.of();
		}
	}

	private Optional<String> resolveChromiumExecutable() {
		if (!configuredExecutable.isEmpty()
				&& processRunner.isCommandAvailable(configuredExecutable, PROBE_TIMEOUT_MS, "--version")) {
			return Optional.of(configuredExecutable);
		}
		List<String> candidates = new ArrayList<>(
				Arrays.asList("google-chrome", "chromium", "chromium-browser", "msedge", "chrome"));
		String os = ExternalScannerSupport.getOsKey();
		if ("macos".equals(os)) {
			candidates.add("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
			candidates.add("/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge");
			candidates.add("/Applications/Chromium.app/Contents/MacOS/Chromium");
		} else if ("windows".equals(os)) {
			candidates.add("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
			candidates.add("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
			candidates.add("C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe");
			candidates.add("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
		} else if ("linux".equals(os)) {
			candidates.add("/usr/bin/google-chrome");
			candidates.add("/usr/bin/chromium");
			candidates.add("/usr/bin/chromium-browser");
			candidates.add("/usr/bin/microsoft-edge");
		}
		return processRunner.resolveFirstAvailable("chromium:" + os, candidates, PROBE_TIMEOUT_MS, "--version");
	}

	private Optional<String> findScanPage() {
		String[] candidates = {"src/main/resources/qrscan_chromium.html", "qrscan_chromium.html",
				System.getProperty("user.dir") + "/src/main/resources/qrscan_chromium.html"};
		return ExternalScannerSupport.resolveSupportFile(candidates, "qrscan_chromium.html", "qrscan_chromium_",
				".html");
	}

	static String extractChromiumJsonPayload(String dumpDom) {
		if (dumpDom == null) {
			return null;
		}
		int errorAt = dumpDom.indexOf("ERROR:");
		if (errorAt >= 0) {
			int end = dumpDom.indexOf("</pre>", errorAt);
			String error = end > errorAt ? dumpDom.substring(errorAt, end) : dumpDom.substring(errorAt);
			log.debug("Chromium scan page reported: {}", error);
			return null;
		}
		int jsonAt = dumpDom.indexOf("JSON:");
		if (jsonAt < 0) {
			return null;
		}
		int end = dumpDom.indexOf("</pre>", jsonAt);
		String payload = end > jsonAt ? dumpDom.substring(jsonAt + 5, end) : dumpDom.substring(jsonAt + 5);
		return payload.trim();
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
