/**
 * PythonBarcodeScanner.java
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

import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner.ProcessResult;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fallback {@link BarcodeScanner} delegating to {@code qrscan_using_python.py}
 * via {@link ExternalProcessRunner} with cached Python availability, timeout
 * enforcement, and separate stderr draining.
 */
public class PythonBarcodeScanner implements BarcodeScanner {

	private static final Logger log = LoggerFactory.getLogger(PythonBarcodeScanner.class);
	private static final long DEFAULT_TIMEOUT_MS = 15000L;
	private static final long PROBE_TIMEOUT_MS = 2000L;
	private static final int MAX_DIMENSION = 750;

	private final ExternalProcessRunner processRunner;
	private final long timeoutMs;

	/**
	 * Creates a {@link PythonBarcodeScanner} backed by the shared
	 * {@link ExternalProcessRunner}.
	 */
	public PythonBarcodeScanner() {
		this(ExternalProcessRunner.shared(), DEFAULT_TIMEOUT_MS);
	}

	/**
	 * Creates a {@link PythonBarcodeScanner} with explicit runner and timeout.
	 *
	 * @param processRunner
	 *            external process runner
	 * @param timeoutMs
	 *            timeout in milliseconds
	 */
	public PythonBarcodeScanner(ExternalProcessRunner processRunner, long timeoutMs) {
		this.processRunner = Objects.requireNonNull(processRunner, "processRunner must not be null");
		this.timeoutMs = timeoutMs > 0 ? timeoutMs : DEFAULT_TIMEOUT_MS;
	}

	@Override
	public String id() {
		return "python";
	}

	@Override
	public boolean isAvailable() {
		return resolvePythonExecutable().isPresent() && findPythonScript().isPresent();
	}

	@Override
	public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
		if (image == null) {
			return List.of();
		}
		Optional<String> pythonExe = resolvePythonExecutable();
		Optional<String> scriptPath = findPythonScript();
		if (pythonExe.isEmpty() || scriptPath.isEmpty()) {
			return List.of();
		}

		BufferedImage processed = scaleAndEnhanceContrast(image);
		try {
			return ExternalProcessRunner.withTempFile("qr_scan_", ".jpg", tempImagePath -> {
				ImageIO.write(processed, "jpg", tempImagePath.toFile());
				ProcessResult result = processRunner
						.run(List.of(pythonExe.get(), scriptPath.get(), tempImagePath.toString()), timeoutMs);
				if (result.timedOut()) {
					log.debug("Python QR scanner timed out after {}ms", timeoutMs);
					return List.of();
				}
				if (!result.isSuccess()) {
					log.debug("Python QR script exited with {}: {}", result.exitCode(), result.stderr().trim());
					return List.of();
				}
				String jsonPayload = ExternalScannerSupport.extractJsonPayload(result.stdout());
				return ExternalScannerSupport.parseQrDetectionsFromJson(jsonPayload, id());
			});
		} catch (IOException e) {
			log.debug("Python QR fallback failed: {}", e.getMessage());
			return List.of();
		}
	}

	private Optional<String> resolvePythonExecutable() {
		return processRunner.resolveFirstAvailable("python-qr", List.of("python3", "python"), PROBE_TIMEOUT_MS,
				"--version");
	}

	private Optional<String> findPythonScript() {
		String[] candidates = {"src/main/resources/qrscan_using_python.py", "qrscan_using_python.py",
				System.getProperty("user.dir") + "/src/main/resources/qrscan_using_python.py"};
		return ExternalScannerSupport.resolveSupportFile(candidates, "qrscan_using_python.py", "qrscan_using_python_",
				".py");
	}

	private BufferedImage scaleAndEnhanceContrast(BufferedImage image) {
		BufferedImage scaledImage = image;
		if (image.getWidth() > MAX_DIMENSION || image.getHeight() > MAX_DIMENSION) {
			double scale = Math.min((double) MAX_DIMENSION / image.getWidth(),
					(double) MAX_DIMENSION / image.getHeight());
			int newWidth = Math.max(1, (int) (image.getWidth() * scale));
			int newHeight = Math.max(1, (int) (image.getHeight() * scale));
			scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = scaledImage.createGraphics();
			try {
				g.drawImage(image, 0, 0, newWidth, newHeight, null);
			} finally {
				g.dispose();
			}
		}

		BufferedImage contrastImage = new BufferedImage(scaledImage.getWidth(), scaledImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < scaledImage.getHeight(); y++) {
			for (int x = 0; x < scaledImage.getWidth(); x++) {
				int rgb = scaledImage.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				if ((r + g + b) > 3 * 180) {
					r = (int) (r * 1.2);
					g = (int) (g * 1.2);
					b = (int) (b * 1.2);
				} else {
					r = (int) (r * 0.8);
					g = (int) (g * 0.8);
					b = (int) (b * 0.8);
				}

				r = Math.min(255, Math.max(0, r));
				g = Math.min(255, Math.max(0, g));
				b = Math.min(255, Math.max(0, b));
				contrastImage.setRGB(x, y, (r << 16) | (g << 8) | b);
			}
		}
		return contrastImage;
	}
}
