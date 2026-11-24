/**
 * TestScanningAndOcrComponents.java
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.harvard.mcz.imagecapture.BulkMediaExporter;
import edu.harvard.mcz.imagecapture.CandidateImageFile;
import edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetector;
import edu.harvard.mcz.imagecapture.ConvertTesseractOCR;
import edu.harvard.mcz.imagecapture.ETHZBarcode;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.MCZENTBarcode;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.TesseractOCR;
import edu.harvard.mcz.imagecapture.data.BulkMedia;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.ocr.OcrEngine;
import edu.harvard.mcz.imagecapture.ocr.OcrResult;
import edu.harvard.mcz.imagecapture.ocr.TesseractCliOcrEngine;
import edu.harvard.mcz.imagecapture.scanning.BarcodeDetection;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScanner;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScannerChain;
import edu.harvard.mcz.imagecapture.scanning.BoofCvBarcodeScanner;
import edu.harvard.mcz.imagecapture.scanning.DefaultPositionTemplateRepository;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner.ProcessResult;
import edu.harvard.mcz.imagecapture.scanning.ImageAnalysisResult;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.ImageMetadata;
import edu.harvard.mcz.imagecapture.scanning.ImagePreprocessor;
import edu.harvard.mcz.imagecapture.scanning.PositionTemplateRepository;
import edu.harvard.mcz.imagecapture.scanning.ScanOptions;
import edu.harvard.mcz.imagecapture.scanning.SpecimenImageAnalyzer;
import edu.harvard.mcz.imagecapture.scanning.TemplateResolver;
import edu.harvard.mcz.imagecapture.scanning.ZxingBarcodeScanner;
import georegression.struct.shapes.Polygon2D_F64;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for Step 2.1 scanning, preprocessing, template resolution, OCR,
 * analysis, and bulk-export components.
 */
public class TestScanningAndOcrComponents {

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

	private File getResourceFile(String resourcePath) {
		URL url = getClass().getResource(resourcePath);
		assertNotNull("Missing test resource: " + resourcePath, url);
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			return new File(url.getPath());
		}
	}

	private BufferedImage renderQrImage(String content, int size, Color darkColor, Color lightColor, String charset)
			throws WriterException {
		QRCodeWriter writer = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
		if (charset != null) {
			hints.put(EncodeHintType.CHARACTER_SET, charset);
		}
		BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? darkColor.getRGB() : lightColor.getRGB());
			}
		}
		return image;
	}

	@Test
	public void testImageMetadataSinglePassAndNullSafety() {
		ImageMetadata img60 = ImageMetadata
				.readFrom(getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG"));
		assertEquals("MCZ-ENT00050008", img60.effectiveUserComment());
		assertEquals("2009-04-22", img60.formatDateCreated("yyyy-MM-dd"));
		assertEquals(2848, img60.getWidth());
		assertEquals(4272, img60.getHeight());

		ImageMetadata ethz = ImageMetadata.readFrom(getResourceFile("/IMG_000069.JPG"));
		assertEquals("", ethz.effectiveUserComment());
		assertEquals("2017-01-30", ethz.formatDateCreated("yyyy-MM-dd"));

		ImageMetadata empty = ImageMetadata.readFrom(getResourceFile(AllTests.FILE_EMPTY));
		assertEquals("", empty.effectiveUserComment());
		assertNull(empty.formatDateCreated("yyyy-MM-dd"));
		assertEquals(0, empty.getWidth());

		ImageMetadata missing = ImageMetadata.readFrom(Paths.get("/nonexistent/file.jpg"));
		assertEquals("", missing.effectiveUserComment());
		assertNull(ImageMetadata.readFrom((Path) null).getDateCreated());

		Date d = new Date(1_700_000_000_000L);
		ImageMetadata fallback = new ImageMetadata("  ", "XMP-DESC", d, 100, 200, 1);
		assertEquals("XMP-DESC", fallback.effectiveUserComment());
		d.setTime(0L);
		assertEquals(1_700_000_000_000L, fallback.getDateCreated().getTime());
	}

	@Test
	public void testImageDocumentLazyLoadingAndReadability() throws Exception {
		Path missing = tempFolder.getRoot().toPath().resolve("missing.jpg");
		ImageDocument missingDoc = ImageDocument.of(missing); // must not throw in constructor
		try {
			missingDoc.ensureReadable();
			fail("Expected UnreadableFileException");
		} catch (UnreadableFileException expected) {
			// expected
		}

		Path imgPath = getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG").toPath();
		ImageDocument doc = ImageDocument.of(imgPath);
		doc.ensureReadable();
		BufferedImage first = doc.getImage();
		BufferedImage second = doc.getImage();
		assertNotNull(first);
		assertSame("Expected cached BufferedImage instance", first, second);
		assertEquals("MCZ-ENT00050008", doc.getMetadata().effectiveUserComment());

		BufferedImage mem = new BufferedImage(50, 40, BufferedImage.TYPE_INT_RGB);
		ImageDocument inMem = ImageDocument.inMemory(mem);
		inMem.ensureReadable();
		assertTrue(inMem.isInMemory());
		assertSame(mem, inMem.getImage());
		assertEquals(50, inMem.getMetadata().getWidth());
		assertEquals(40, inMem.getMetadata().getHeight());
	}

	@Test
	public void testExternalProcessRunnerTimeoutDrainingAndTempCleanup() throws Exception {
		ExternalProcessRunner runner = new ExternalProcessRunner();
		ProcessResult ok = runner.run(List.of("/bin/sh", "-c", "printf 'out1'; printf 'err1' >&2"), 3000L);
		assertTrue(ok.isSuccess());
		assertEquals("out1", ok.stdout());
		assertEquals("err1", ok.stderr());
		assertFalse(ok.timedOut());

		// Heavy concurrent stdout + stderr output must not deadlock pipes
		ProcessResult heavy = runner.run(
				List.of("/bin/sh", "-c", "i=0; while [ $i -lt 500 ]; do printf '123456789012345678901234567890\\n'; "
						+ "printf 'err12345678901234567890123456\\n' >&2; i=$((i+1)); done"),
				5000L);
		assertTrue(heavy.isSuccess());
		assertTrue(heavy.stdout().length() > 10000);
		assertTrue(heavy.stderr().length() > 10000);

		ProcessResult timedOut = runner.run(List.of("/bin/sh", "-c", "sleep 5"), 150L);
		assertTrue(timedOut.timedOut());
		assertFalse(timedOut.isSuccess());

		assertTrue(runner.isCommandAvailable("/bin/sh", 1000L, "-c", "exit 0"));
		runner.clearCache();

		final Path[] captured = new Path[1];
		try {
			ExternalProcessRunner.withTempFile("test_cleanup_", ".tmp", path -> {
				captured[0] = path;
				assertTrue(Files.exists(path));
				throw new IOException("simulated failure");
			});
			fail("Expected IOException");
		} catch (IOException expected) {
			assertFalse("Temp file must be deleted in finally", Files.exists(captured[0]));
		}
	}

	@Test
	public void testImagePreprocessorNonMutatingAndBoundsSafe() {
		BufferedImage parent = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = parent.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.BLACK);
		g.fillRect(10, 10, 20, 20);
		g.dispose();

		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(-1, 0, 10, 10)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(90, 90, 20, 20)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(0, 0, 0, 10)).isEmpty());

		BufferedImage crop = ImagePreprocessor.copySubimage(parent, new Rectangle(10, 10, 20, 20)).orElseThrow();
		// Mutate crop and verify parent pixel is unaffected
		crop.setRGB(5, 5, Color.RED.getRGB());
		assertEquals(Color.BLACK.getRGB(), parent.getRGB(15, 15));

		// Unusual image types should work cleanly with sharpen &
		// adjustBrightnessContrast
		BufferedImage indexed = new BufferedImage(40, 40, BufferedImage.TYPE_BYTE_INDEXED);
		BufferedImage sharpened = ImagePreprocessor.sharpen(indexed);
		assertEquals(40, sharpened.getWidth());
		BufferedImage adjusted = ImagePreprocessor.adjustBrightnessContrast(indexed, 1.2f, 15f);
		assertEquals(40, adjusted.getWidth());

		// Multi-modifier transform spec applies both sharpen and brighter without
		// mutating crop
		BufferedImage transformed = ImagePreprocessor.applyTransformSpec(crop, "40sharpenbrighter", 800d).orElseThrow();
		assertEquals(40, transformed.getWidth());
		assertEquals(40, transformed.getHeight());

		List<Rectangle> edgeDisplaced = ImagePreprocessor.displacedRegions(new Rectangle(0, 0, 20, 20), 50, 50);
		assertFalse(edgeDisplaced.isEmpty());
		for (Rectangle r : edgeDisplaced) {
			assertTrue(r.x >= 0 && r.y >= 0 && r.x + r.width <= 50 && r.y + r.height <= 50);
		}
	}

	@Test
	public void testZxingAndBoofCvScannersAndNoTempCropSideEffect() throws Exception {
		Path cwdTempCrop = Paths.get("TempBarcodeCrop.png");
		Files.deleteIfExists(cwdTempCrop);

		// QuickCheck displacement test (QR shifted by +3px in x & y)
		BufferedImage canvas = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = canvas.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 300, 300);
		BufferedImage qr = renderQrImage("ETHZ-ENT0012345", 160, Color.BLACK, Color.WHITE, null);
		g.drawImage(qr, 53, 53, null);
		g.dispose();

		ZxingBarcodeScanner zxing = new ZxingBarcodeScanner();
		List<BarcodeDetection> quickFound = zxing.detect(canvas,
				ScanOptions.builder().preferredRegion(new Rectangle(50, 50, 160, 160)).quickCheck(true).build());
		assertFalse("quickCheck displacement fallback should find shifted QR", quickFound.isEmpty());
		assertEquals("ETHZ-ENT0012345", quickFound.get(0).text());
		assertFalse("ZxingBarcodeScanner must not create TempBarcodeCrop.png", Files.exists(cwdTempCrop));

		// Out-of-bounds negative region must return empty instead of throwing
		assertTrue(zxing.detect(canvas, ScanOptions.builder().preferredRegion(new Rectangle(-10, 0, 50, 50)).build())
				.isEmpty());

		// BoofCV rotated polygon bounds should span all 4 vertices
		Polygon2D_F64 rotatedPoly = new Polygon2D_F64(4);
		rotatedPoly.set(0, 100, 20);
		rotatedPoly.set(1, 180, 60);
		rotatedPoly.set(2, 140, 140);
		rotatedPoly.set(3, 60, 100);
		Rectangle polyBox = BoofCvBarcodeScanner.polygonBounds(rotatedPoly);
		assertEquals(new Rectangle(60, 20, 120, 120), polyBox);

		BoofCvBarcodeScanner boofCv = new BoofCvBarcodeScanner();
		List<BarcodeDetection> boofAll = boofCv.detect(canvas, ScanOptions.defaults());
		assertEquals(1, boofAll.size());
		assertEquals("ETHZ-ENT0012345", boofAll.get(0).text());
		assertTrue(boofAll.get(0).bounds().isPresent());
	}

	@Test
	public void testPythonScriptHasNoRuntimePipInstall() throws IOException {
		String py = Files.readString(Paths.get("src/main/resources/qrscan_using_python.py"), StandardCharsets.UTF_8);
		assertFalse("qrscan_using_python.py must not invoke pip install at runtime", py.contains("pip"));
		assertTrue(py.contains("sys.exit(1)"));
	}

	@Test
	public void testTemplateResolverScanOnceAndConfiguredDefaultFix() throws Exception {
		UnitTrayLabel label = new UnitTrayLabel();
		label.setFamily("Papilionidae");
		label.setGenus("Papilio");
		label.setSpecificEpithet("machaon");
		label.setCollection("ETHZ-ENT");

		BufferedImage canvas = new BufferedImage(2848, 4272, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = canvas.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 2848, 4272);
		g.drawImage(renderQrImage("ETHZ-ENT0012345", 260, Color.BLACK, Color.WHITE, null), 2510, 110, null);
		g.drawImage(renderQrImage(label.toJSONString(), 500, Color.BLACK, Color.WHITE, StandardCharsets.UTF_8.name()),
				1300, 150, null);
		g.dispose();

		AtomicInteger scanCalls = new AtomicInteger();
		BarcodeScanner countingScanner = new BarcodeScanner() {
			private final BoofCvBarcodeScanner delegate = new BoofCvBarcodeScanner();

			@Override
			public String id() {
				return "counting-boofcv";
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
				scanCalls.incrementAndGet();
				return delegate.detect(image, options);
			}
		};

		Singleton.getSingletonInstance().getProperties().getProperties()
				.setProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES, PositionTemplate.TEMPLATE_DEFAULT);
		PositionTemplateRepository repo = new DefaultPositionTemplateRepository();
		assertFalse(repo.findAll().stream()
				.anyMatch(t -> PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(t.getTemplateId())));

		// Pass a fresh String("Default template") so reference equality (!=) would fail
		// if not fixed to .equals
		String nonInternedDefault = new String(PositionTemplate.TEMPLATE_DEFAULT);
		TemplateResolver resolver = new TemplateResolver(repo, new ETHZBarcode(),
				new BarcodeScannerChain(List.of(countingScanner)), nonInternedDefault);
		assertFalse(resolver.hasExplicitDefaultTemplate());

		PositionTemplate resolved = resolver.resolveTemplate(canvas, null, true);
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, resolved.getTemplateId());
		assertEquals("Scan-once should resolve template in 1 scanner call", 1, scanCalls.get());

		SpecimenImageAnalyzer analyzer = new SpecimenImageAnalyzer(new BarcodeScannerChain(List.of(countingScanner)),
				resolver, new ETHZBarcode(), null, false);
		ImageAnalysisResult result = analyzer.analyze(ImageDocument.inMemory(canvas));
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, result.getTemplate().getTemplateId());
		assertEquals("ETHZ-ENT0012345", result.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, result.getCatalogBarcodeStatus());
		assertNotNull(result.getUnitTrayLabel());
		assertEquals("Papilionidae", result.getUnitTrayLabel().getFamily());
		assertFalse(result.getOcrText().isPresent());
	}

	@Test
	public void testOcrPropertiesEngineAndLegacyWrappers() throws Exception {
		ImageCaptureProperties props = new ImageCaptureProperties();
		assertEquals("false", props.getProperties().getProperty(ImageCaptureProperties.KEY_OCR_ENABLED));
		assertEquals("tesseract", props.getProperties().getProperty(ImageCaptureProperties.KEY_TESSERACT_EXECUTABLE));

		List<List<String>> capturedCommands = new ArrayList<>();
		List<Path> createdInputFiles = new ArrayList<>();
		List<Path> createdOutputBases = new ArrayList<>();

		ExternalProcessRunner fakeRunner = new ExternalProcessRunner() {
			@Override
			public boolean isCommandAvailable(String executable, long timeoutMs, String... probeArgs) {
				return true;
			}

			@Override
			public ProcessResult run(List<String> command, long timeoutMs, boolean mergeErrorStream)
					throws IOException {
				capturedCommands.add(command);
				Path inputPath = Paths.get(command.get(1));
				Path outputBase = Paths.get(command.get(2));
				createdInputFiles.add(inputPath);
				createdOutputBases.add(outputBase);
				assertTrue(Files.exists(inputPath));
				Files.writeString(Paths.get(outputBase.toString() + ".txt"), "Danaus $plexippus@|_\n",
						StandardCharsets.UTF_8);
				return new ProcessResult(0, "", "", false);
			}
		};

		TesseractCliOcrEngine engine = new TesseractCliOcrEngine(fakeRunner, "  tesseract  ", 5000L);
		assertTrue(engine.isAvailable());
		BufferedImage sample = new BufferedImage(30, 20, BufferedImage.TYPE_INT_RGB);
		OcrResult res = engine.recognize(sample, "eng");
		assertTrue(res.isSuccess());
		assertEquals("Danaus plexippus\n", res.getText());

		assertEquals(1, capturedCommands.size());
		List<String> argv = capturedCommands.get(0);
		assertEquals("tesseract", argv.get(0));
		assertEquals("-l", argv.get(3));
		assertEquals("eng", argv.get(4));
		assertFalse("Input temp PNG must be deleted", Files.exists(createdInputFiles.get(0)));
		assertFalse("Output base temp file must be deleted", Files.exists(createdOutputBases.get(0)));
		assertFalse("Output .txt temp file must be deleted",
				Files.exists(Paths.get(createdOutputBases.get(0).toString() + ".txt")));

		// Legacy wrappers preserve OCRReadException contract and delegate cleanly
		ConvertTesseractOCR convertWrapper = new ConvertTesseractOCR(null);
		try {
			convertWrapper.getOCRText();
			fail("Expected OCRReadException for null image");
		} catch (OCRReadException expected) {
			// expected
		}

		TesseractOCR fileWrapper = new TesseractOCR();
		try {
			fileWrapper.setTarget("/nonexistent/ocr_input.tif");
			fail("Expected OCRReadException for unreadable file");
		} catch (OCRReadException expected) {
			// expected
		}
	}

	@Test
	public void testSpecimenAnalyzerRealFixtureAndSmallImageOcrBoundsGuard() throws Exception {
		// Disable external providers for deterministic core scan
		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES, PositionTemplate.TEMPLATE_DEFAULT);
		props.setProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false");
		Singleton.getSingletonInstance().setBarcodeMatcher(new MCZENTBarcode());
		Singleton.getSingletonInstance().setBarcodeBuilder(new MCZENTBarcode());

		SpecimenImageAnalyzer analyzer = SpecimenImageAnalyzer.fromSingleton();
		Path img60 = getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG").toPath();
		ImageAnalysisResult result = analyzer.analyze(img60);
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, result.getTemplate().getTemplateId());
		assertEquals("MCZ-ENT00050008", result.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, result.getCatalogBarcodeStatus());
		assertEquals("MCZ-ENT00050008", result.getMetadata().effectiveUserComment());
		assertFalse(result.getOcrText().isPresent());

		// Gated OCR on small image with explicit component template must not throw
		// RasterFormatException
		OcrEngine neverCalled = new OcrEngine() {
			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public OcrResult recognize(BufferedImage region, String language) {
				fail("OCR should not run when text region lies outside small image");
				return OcrResult.failure("unexpected");
			}
		};
		TemplateResolver fixedResolver = new TemplateResolver(new DefaultPositionTemplateRepository(),
				new MCZENTBarcode(), BarcodeScannerChain.defaultChain(props), PositionTemplate.TEMPLATE_TEST_1);
		SpecimenImageAnalyzer ocrAnalyzer = new SpecimenImageAnalyzer(BarcodeScannerChain.defaultChain(props),
				fixedResolver, new MCZENTBarcode(), neverCalled, true);
		ImageAnalysisResult smallResult = ocrAnalyzer
				.analyze(ImageDocument.inMemory(new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)));
		assertFalse(smallResult.getOcrText().isPresent());
		assertFalse(smallResult.getDiagnostics().isEmpty());

		// BulkMediaExporter integration check
		String csvLine = BulkMediaExporter.parseOneFile(img60.toString());
		assertEquals("\"IMG_000060.JPG\",\"MCZ-ENT00050008\"", csvLine);

		BulkMedia bulk = BulkMediaExporter.parseOneFileToBulkMedia(img60.toString());
		assertNotNull(bulk);
		List<String> row = bulk.getData();
		assertEquals(6, row.size());
		assertEquals("shows cataloged_item=MCZ:Ent:50008", row.get(3));
		assertTrue(row.get(5).contains("original filename=IMG_000060.JPG"));
	}

	@Test
	public void testCandidateImageFileFacadeAndConfiguredTemplateDetector() throws Exception {
		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false");
		// Non-interned default template string verifies reference-equality fix (!= ->
		// !.equals)
		props.setProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES, new String(PositionTemplate.TEMPLATE_DEFAULT));
		props.setProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false");
		Singleton.getSingletonInstance().setBarcodeMatcher(new MCZENTBarcode());
		Singleton.getSingletonInstance().setBarcodeBuilder(new MCZENTBarcode());

		File img60File = getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG");

		// ConfiguredBarcodePositionTemplateDetector.detectTemplateForImage(File) must
		// handle scannableFile == null cleanly
		ConfiguredBarcodePositionTemplateDetector detector = new ConfiguredBarcodePositionTemplateDetector();
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, detector.detectTemplateForImage(img60File));
		assertEquals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS,
				detector.detectTemplateForImage(getResourceFile(AllTests.FILE_EMPTY)));

		// CandidateImageFile(File) single-pass facade
		CandidateImageFile cif60 = new CandidateImageFile(img60File);
		assertNotNull(cif60.getImageDocument());
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, cif60.getTemplateUsed().getTemplateId());
		assertEquals("MCZ-ENT00050008", cif60.getBarcodeTextAtFoundTemplate());
		assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, cif60.getCatalogNumberBarcodeStatus());
		assertEquals("MCZ-ENT00050008", cif60.getExifUserCommentText());
		assertEquals("2009-04-22", cif60.getExifDateCreated("yyyy-MM-dd"));
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, detector.detectTemplateForImage(cif60));

		// Dual-QR synthetic file through CandidateImageFile(File)
		Singleton.getSingletonInstance().setBarcodeMatcher(new ETHZBarcode());
		Singleton.getSingletonInstance().setBarcodeBuilder(new ETHZBarcode());
		UnitTrayLabel label = new UnitTrayLabel();
		label.setFamily("Papilionidae");
		label.setGenus("Papilio");
		label.setSpecificEpithet("machaon");
		label.setCollection("ETHZ-ENT");

		BufferedImage canvas = new BufferedImage(2848, 4272, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = canvas.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 2848, 4272);
		g.drawImage(renderQrImage("ETHZ-ENT0012345", 260, Color.BLACK, Color.WHITE, null), 2510, 110, null);
		g.drawImage(renderQrImage(label.toJSONString(), 500, Color.BLACK, Color.WHITE, StandardCharsets.UTF_8.name()),
				1300, 150, null);
		g.dispose();
		File dualFile = tempFolder.newFile("facade_dual_qr.png");
		ImageIO.write(canvas, "png", dualFile);

		CandidateImageFile dualCif = new CandidateImageFile(dualFile);
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, dualCif.getTemplateUsed().getTemplateId());
		assertEquals("ETHZ-ENT0012345", dualCif.getBarcodeTextAtFoundTemplate());
		assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, dualCif.getCatalogNumberBarcodeStatus());
		assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, dualCif.getUnitTrayTaxonLabelTextStatus());
		assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, dualCif.getBarcodeStatus());
		UnitTrayLabel extracted = dualCif.getTaxonLabelQRText(dualCif.getTemplateUsed());
		assertNotNull(extracted);
		assertEquals("Papilionidae", extracted.getFamily());
		assertEquals("Papilio", extracted.getGenus());

		// Wrong image width for template returns diagnostic string + RESULT_ERROR
		CandidateImageFile smallCif = new CandidateImageFile(getResourceFile(AllTests.FILE_VALID_BARCODE),
				new PositionTemplate(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS));
		assertEquals("Image is different size from Template.",
				smallCif.getBarcodeText(new PositionTemplate(PositionTemplate.TEMPLATE_DEFAULT)));
		assertEquals(CandidateImageFile.RESULT_ERROR, smallCif.getCatalogNumberBarcodeStatus());
		assertNull(smallCif.getTaxonLabelOCRText(new PositionTemplate(PositionTemplate.TEMPLATE_TEST_1)));

		// Forwarding helper
		CandidateImageFile.debugCheckHeightWidth(img60File);
	}
}
