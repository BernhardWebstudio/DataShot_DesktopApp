/**
 * TesseractCliOcrEngine.java
 * edu.harvard.mcz.imagecapture.ocr
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
package edu.harvard.mcz.imagecapture.ocr;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner.ProcessResult;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe {@link OcrEngine} invoking the Tesseract CLI via
 * {@link ExternalProcessRunner} with isolated temporary PNG/output files and
 * guaranteed cleanup.
 */
public class TesseractCliOcrEngine implements OcrEngine {

	public static final String DEFAULT_LANGUAGE = "lepid";
	private static final Logger log = LoggerFactory.getLogger(TesseractCliOcrEngine.class);
	private static final long DEFAULT_TIMEOUT_MS = 30000L;
	private static final long PROBE_TIMEOUT_MS = 2500L;

	private final ExternalProcessRunner processRunner;
	private final Supplier<String> executableSupplier;
	private final long timeoutMs;

	/**
	 * Creates an engine reading the Tesseract executable dynamically from
	 * {@link Singleton} properties.
	 */
	public TesseractCliOcrEngine() {
		this(ExternalProcessRunner.shared(), TesseractCliOcrEngine::readSingletonExecutable, DEFAULT_TIMEOUT_MS);
	}

	/**
	 * Creates an engine with an explicit executable and timeout.
	 *
	 * @param processRunner
	 *            external process runner
	 * @param executable
	 *            tesseract executable name or path
	 * @param timeoutMs
	 *            process timeout in milliseconds
	 */
	public TesseractCliOcrEngine(ExternalProcessRunner processRunner, String executable, long timeoutMs) {
		this(processRunner, () -> executable, timeoutMs);
	}

	/**
	 * Creates an engine with a supplier for the executable path.
	 *
	 * @param processRunner
	 *            external process runner
	 * @param executableSupplier
	 *            supplier providing the tesseract executable string
	 * @param timeoutMs
	 *            process timeout in milliseconds
	 */
	public TesseractCliOcrEngine(ExternalProcessRunner processRunner, Supplier<String> executableSupplier,
			long timeoutMs) {
		this.processRunner = Objects.requireNonNull(processRunner, "processRunner must not be null");
		this.executableSupplier = Objects.requireNonNull(executableSupplier, "executableSupplier must not be null");
		this.timeoutMs = timeoutMs > 0 ? timeoutMs : DEFAULT_TIMEOUT_MS;
	}

	@Override
	public boolean isAvailable() {
		String executable = resolvedExecutable();
		if (executable.isEmpty()) {
			return false;
		}
		return processRunner.isCommandAvailable(executable, PROBE_TIMEOUT_MS, "--version");
	}

	@Override
	public OcrResult recognize(BufferedImage region, String language) {
		if (region == null) {
			return OcrResult.failure("Image region for OCR is null.");
		}
		Path inputPath = null;
		try {
			inputPath = Files.createTempFile("ocr_input_", ".png");
			if (!ImageIO.write(region, "png", inputPath.toFile())) {
				return OcrResult.failure("No PNG ImageWriter available for OCR input.");
			}
			return recognizeFile(inputPath, language);
		} catch (IOException e) {
			log.error("Failed preparing temporary PNG for OCR: {}", e.getMessage());
			return OcrResult.failure("Temporary OCR image creation failed: " + e.getMessage());
		} finally {
			ExternalProcessRunner.deleteQuietly(inputPath);
		}
	}

	/**
	 * Runs Tesseract against an existing image file on disk.
	 *
	 * @param imageFile
	 *            path to an existing readable image file
	 * @param language
	 *            Tesseract language code (defaults to {@link #DEFAULT_LANGUAGE})
	 * @return immutable {@link OcrResult}
	 */
	public OcrResult recognizeFile(Path imageFile, String language) {
		if (imageFile == null || !Files.isReadable(imageFile)) {
			return OcrResult.failure("Can't read OCR input file: " + imageFile);
		}
		String executable = resolvedExecutable();
		if (executable.isEmpty()) {
			return OcrResult.failure("Tesseract executable is not configured.");
		}
		String effectiveLang = normalizeLanguage(language);

		Path outputBase = null;
		Path outputTxt = null;
		try {
			outputBase = Files.createTempFile("ocr_out_", "");
			outputTxt = Paths.get(outputBase.toString() + ".txt");

			List<String> command = List.of(executable, imageFile.toAbsolutePath().toString(),
					outputBase.toAbsolutePath().toString(), "-l", effectiveLang);
			log.debug("Running OCR command: {}", command);

			ProcessResult result = processRunner.run(command, timeoutMs);
			if (result.timedOut()) {
				return OcrResult.failure("OCR process timed out after " + timeoutMs + "ms.");
			}
			if (result.exitCode() != 0) {
				String err = result.stderr().isBlank() ? "" : " " + result.stderr().trim();
				return OcrResult.failure("OCR process failed (exit value " + result.exitCode() + ")." + err);
			}
			if (!Files.exists(outputTxt)) {
				return OcrResult.failure("OCR output file was not produced.");
			}
			String rawText = Files.readString(outputTxt, StandardCharsets.UTF_8);
			// $ is returned by tesseract to indicate italics
			// @ is returned by tesseract to indicate bold
			// | and _ are common interpretations of QR barcode blocks
			String cleaned = rawText.replaceAll("[|@\\$_]", "");
			return OcrResult.success(cleaned);
		} catch (IOException e) {
			log.error("OCR process failed with IOException: {}", e.getMessage());
			return OcrResult.failure("OCR process failed with IO Exception: " + e.getMessage());
		} finally {
			ExternalProcessRunner.deleteQuietly(outputTxt);
			ExternalProcessRunner.deleteQuietly(outputBase);
		}
	}

	private String resolvedExecutable() {
		String raw = executableSupplier.get();
		if (raw == null) {
			return "";
		}
		return raw.trim();
	}

	static String normalizeLanguage(String language) {
		if (language == null || language.isBlank()) {
			return DEFAULT_LANGUAGE;
		}
		String trimmed = language.trim();
		if (trimmed.startsWith("-l ")) {
			trimmed = trimmed.substring(3).trim();
		} else if ("-l".equals(trimmed)) {
			return DEFAULT_LANGUAGE;
		}
		return trimmed.isEmpty() ? DEFAULT_LANGUAGE : trimmed;
	}

	private static String readSingletonExecutable() {
		try {
			String configured = Singleton.getSingletonInstance().getProperties().getProperties()
					.getProperty(ImageCaptureProperties.KEY_TESSERACT_EXECUTABLE, "tesseract");
			return configured == null ? "tesseract" : configured.trim();
		} catch (Exception e) {
			return "tesseract";
		}
	}
}
