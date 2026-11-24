/**
 * TestOcrEngineAndGating.java
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

import edu.harvard.mcz.imagecapture.CandidateImageFile;
import edu.harvard.mcz.imagecapture.ConvertTesseractOCR;
import edu.harvard.mcz.imagecapture.ETHZBarcode;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.TesseractOCR;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.ocr.OcrEngine;
import edu.harvard.mcz.imagecapture.ocr.OcrResult;
import edu.harvard.mcz.imagecapture.ocr.TesseractCliOcrEngine;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScannerChain;
import edu.harvard.mcz.imagecapture.scanning.DefaultPositionTemplateRepository;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner;
import edu.harvard.mcz.imagecapture.scanning.ImageAnalysisResult;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.SpecimenImageAnalyzer;
import edu.harvard.mcz.imagecapture.scanning.TemplateResolver;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for OCR properties, gating, {@link TesseractCliOcrEngine}, and
 * legacy OCR wrappers ({@link ConvertTesseractOCR}, {@link TesseractOCR}).
 */
public class TestOcrEngineAndGating {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Properties savedProperties;
	private BarcodeMatcher savedMatcher;
	private BarcodeBuilder savedBuilder;

	@Before
	public void setUp() {
		Properties current = Singleton.getSingletonInstance().getProperties().getProperties();
		savedProperties = new Properties();
		savedProperties.putAll(current);
		savedMatcher = Singleton.getSingletonInstance().getBarcodeMatcher();
		savedBuilder = Singleton.getSingletonInstance().getBarcodeBuilder();
	}

	@After
	public void tearDown() {
		Properties current = Singleton.getSingletonInstance().getProperties().getProperties();
		current.clear();
		current.putAll(savedProperties);
		Singleton.getSingletonInstance().setBarcodeMatcher(savedMatcher);
		Singleton.getSingletonInstance().setBarcodeBuilder(savedBuilder);
		ExternalProcessRunner.shared().clearCache();
	}

	private Path createMockTesseractScript(Path argvLog, Path createdFilesLog) throws Exception {
		Path script = tempFolder.getRoot().toPath().resolve("mock_tesseract.sh");
		String body = "#!/bin/sh\n" + "if [ \"$1\" = \"--version\" ]; then\n" + "  printf 'tesseract 5.0.0\\n'\n"
				+ "  exit 0\n" + "fi\n" + "for arg in \"$@\"; do\n" + "  printf '%s\\n' \"$arg\" >> '"
				+ argvLog.toAbsolutePath() + "'\n" + "done\n" + "printf 'INPUT=%s\\nOUT=%s\\n' \"$1\" \"$2\" >> '"
				+ createdFilesLog.toAbsolutePath() + "'\n" + "printf 'Danaus $plexippus@|_\\n' > \"$2.txt\"\n"
				+ "exit 0\n";
		Files.writeString(script, body, StandardCharsets.UTF_8);
		assertTrue(script.toFile().setExecutable(true));
		return script;
	}

	@Test
	public void testImageCapturePropertiesDefaults() {
		ImageCaptureProperties defaults = new ImageCaptureProperties();
		assertEquals("false", defaults.getProperties().getProperty(ImageCaptureProperties.KEY_OCR_ENABLED));
		assertEquals("tesseract",
				defaults.getProperties().getProperty(ImageCaptureProperties.KEY_TESSERACT_EXECUTABLE));
	}

	@Test
	public void testOcrGatingInAnalyzerAndCandidateImageFile() throws Exception {
		Path argvLog = tempFolder.getRoot().toPath().resolve("gating_argv.txt");
		Path filesLog = tempFolder.getRoot().toPath().resolve("gating_files.txt");
		Path mockScript = createMockTesseractScript(argvLog, filesLog);

		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_TESSERACT_EXECUTABLE, mockScript.toAbsolutePath().toString());
		Singleton.getSingletonInstance().setBarcodeMatcher(new ETHZBarcode());
		Singleton.getSingletonInstance().setBarcodeBuilder(new ETHZBarcode());

		BufferedImage largeBlank = new BufferedImage(2848, 4272, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = largeBlank.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 2848, 4272);
		g.dispose();

		File largeFile = tempFolder.newFile("ocr_gating_blank.png");
		ImageIO.write(largeBlank, "png", largeFile);

		// CandidateImageFile(File, PositionTemplate) and setFile(...) must skip OCR
		// when KEY_OCR_ENABLED=false
		PositionTemplate template = new PositionTemplate(PositionTemplate.TEMPLATE_DEFAULT);
		CandidateImageFile cif = new CandidateImageFile(largeFile, template);
		cif.setFile(largeFile, template);
		assertFalse("Mock tesseract must not be invoked when ocr.enabled=false", Files.exists(argvLog));

		// SpecimenImageAnalyzer: ocrEnabled=false skips OCR, ocrEnabled=true runs OCR
		// when UnitTrayLabel QR is absent
		AtomicInteger ocrCalls = new AtomicInteger();
		OcrEngine countingOcr = new OcrEngine() {
			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public OcrResult recognize(BufferedImage region, String language) {
				ocrCalls.incrementAndGet();
				return OcrResult.success("Papilio machaon");
			}
		};

		TemplateResolver fixedResolver = new TemplateResolver(new DefaultPositionTemplateRepository(),
				new ETHZBarcode(), BarcodeScannerChain.defaultChain(props), PositionTemplate.TEMPLATE_TEST_1);
		SpecimenImageAnalyzer disabledAnalyzer = new SpecimenImageAnalyzer(BarcodeScannerChain.defaultChain(props),
				fixedResolver, new ETHZBarcode(), countingOcr, false);
		ImageAnalysisResult disabledRes = disabledAnalyzer.analyze(ImageDocument.inMemory(largeBlank));
		assertFalse(disabledRes.getOcrText().isPresent());
		assertEquals(0, ocrCalls.get());

		SpecimenImageAnalyzer enabledAnalyzer = new SpecimenImageAnalyzer(BarcodeScannerChain.defaultChain(props),
				fixedResolver, new ETHZBarcode(), countingOcr, true);
		ImageAnalysisResult enabledRes = enabledAnalyzer.analyze(ImageDocument.inMemory(largeBlank));
		assertEquals("Papilio machaon", enabledRes.getOcrText().orElse(""));
		assertEquals(1, ocrCalls.get());

		// Small image must be guarded against RasterFormatException when OCR is enabled
		ImageAnalysisResult smallRes = enabledAnalyzer
				.analyze(ImageDocument.inMemory(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)));
		assertFalse(smallRes.getOcrText().isPresent());
		assertEquals(1, ocrCalls.get());
		assertFalse(smallRes.getDiagnostics().isEmpty());
	}

	@Test
	public void testTesseractCliOcrEngineWithRealMockExecutableAndCleanup() throws Exception {
		Path argvLog = tempFolder.getRoot().toPath().resolve("cli_argv.txt");
		Path filesLog = tempFolder.getRoot().toPath().resolve("cli_files.txt");
		Path mockScript = createMockTesseractScript(argvLog, filesLog);

		TesseractCliOcrEngine engine = new TesseractCliOcrEngine(new ExternalProcessRunner(),
				mockScript.toAbsolutePath().toString(), 5000L);
		assertTrue(engine.isAvailable());

		BufferedImage img = new BufferedImage(40, 20, BufferedImage.TYPE_INT_RGB);
		OcrResult result = engine.recognize(img, "eng");
		assertTrue(result.isSuccess());
		assertEquals("Danaus plexippus\n", result.getText());

		List<String> argv = Files.readAllLines(argvLog, StandardCharsets.UTF_8);
		assertEquals(4, argv.size());
		assertEquals("-l", argv.get(2));
		assertEquals("eng", argv.get(3));

		// Default language when null/blank
		Files.deleteIfExists(argvLog);
		OcrResult defaultLang = engine.recognize(img, null);
		assertTrue(defaultLang.isSuccess());
		List<String> defaultArgv = Files.readAllLines(argvLog, StandardCharsets.UTF_8);
		assertEquals("-l", defaultArgv.get(2));
		assertEquals(TesseractCliOcrEngine.DEFAULT_LANGUAGE, defaultArgv.get(3));

		// Verify all temp input PNG, output base, and .txt files were cleaned up
		List<String> lines = Files.readAllLines(filesLog, StandardCharsets.UTF_8);
		for (String line : lines) {
			if (line.startsWith("INPUT=")) {
				Path p = Paths.get(line.substring("INPUT=".length()));
				assertFalse("Input temp PNG must be deleted: " + p, Files.exists(p));
			} else if (line.startsWith("OUT=")) {
				Path base = Paths.get(line.substring("OUT=".length()));
				assertFalse("Output base temp file must be deleted: " + base, Files.exists(base));
				assertFalse("Output .txt temp file must be deleted", Files.exists(Paths.get(base + ".txt")));
			}
		}
	}

	@Test
	public void testLegacyConvertTesseractOcrAndTesseractOcrEndToEndWithoutCwdTempFiles() throws Exception {
		Files.deleteIfExists(Paths.get("temptiff.tif"));
		Files.deleteIfExists(Paths.get("temptiff.jpg"));
		Files.deleteIfExists(Paths.get("tempOCRtextOutput.txt"));

		Path argvLog = tempFolder.getRoot().toPath().resolve("legacy_argv.txt");
		Path filesLog = tempFolder.getRoot().toPath().resolve("legacy_files.txt");
		Path mockScript = createMockTesseractScript(argvLog, filesLog);

		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_TESSERACT_EXECUTABLE, "  " + mockScript.toAbsolutePath() + "  ");

		BufferedImage sample = new BufferedImage(40, 20, BufferedImage.TYPE_INT_RGB);
		ConvertTesseractOCR convert = new ConvertTesseractOCR(sample);
		assertEquals("Danaus plexippus\n", convert.getOCRText());

		File imageFile = tempFolder.newFile("legacy_ocr_input.png");
		ImageIO.write(sample, "png", imageFile);
		TesseractOCR fileOcr = new TesseractOCR();
		fileOcr.setTarget(imageFile.getAbsolutePath());
		assertEquals("Danaus plexippus\n", fileOcr.getOCRText());

		assertFalse("temptiff.tif must not be created in cwd", Files.exists(Paths.get("temptiff.tif")));
		assertFalse("temptiff.jpg must not be created in cwd", Files.exists(Paths.get("temptiff.jpg")));
		assertFalse("tempOCRtextOutput.txt must not be created in cwd",
				Files.exists(Paths.get("tempOCRtextOutput.txt")));
	}
}
