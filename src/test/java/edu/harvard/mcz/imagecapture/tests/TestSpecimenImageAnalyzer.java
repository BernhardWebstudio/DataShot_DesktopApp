/**
 * TestSpecimenImageAnalyzer.java
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

import edu.harvard.mcz.imagecapture.ETHZBarcode;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.MCZENTBarcode;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.ocr.OcrEngine;
import edu.harvard.mcz.imagecapture.ocr.OcrResult;
import edu.harvard.mcz.imagecapture.scanning.BarcodeDetection;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScanner;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScannerChain;
import edu.harvard.mcz.imagecapture.scanning.DefaultPositionTemplateRepository;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner;
import edu.harvard.mcz.imagecapture.scanning.ImageAnalysisResult;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.PositionTemplateRepository;
import edu.harvard.mcz.imagecapture.scanning.ScanOptions;
import edu.harvard.mcz.imagecapture.scanning.SpecimenImageAnalyzer;
import edu.harvard.mcz.imagecapture.scanning.TemplateResolver;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * End-to-end unit tests for {@link SpecimenImageAnalyzer} using mock
 * collaborators and real fixture images without database or Swing dependencies.
 */
public class TestSpecimenImageAnalyzer {

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

	private static PositionTemplate makeTemplate(String id, int w, int h) {
		PositionTemplate t = new PositionTemplate();
		t.setTemplateId(id);
		t.setTemplateName(id);
		t.setImageSize(new Dimension(w, h));
		t.setBarcodePosition(new Dimension(700, 50));
		t.setBarcodeSize(new Dimension(200, 200));
		t.setUtBarcodePosition(new Dimension(300, 50));
		t.setUtBarcodeSize(new Dimension(250, 250));
		t.setTextPosition(new Dimension(50, 50));
		t.setTextSize(new Dimension(200, 100));
		return t;
	}

	private static PositionTemplateRepository inMemoryRepo(PositionTemplate t) {
		return new PositionTemplateRepository() {
			@Override
			public List<PositionTemplate> findAll() {
				return List.of(t);
			}

			@Override
			public Optional<PositionTemplate> findById(String templateId) {
				return t.getTemplateId().equals(templateId) ? Optional.of(t) : Optional.empty();
			}
		};
	}

	@Test
	public void testEndToEndWithInjectedCollaboratorsCatalogUnitTrayOcrAndDiagnostics() throws Exception {
		PositionTemplate tpl = makeTemplate("MOCK_TPL", 1000, 1000);
		PositionTemplateRepository repo = inMemoryRepo(tpl);

		UnitTrayLabel label = new UnitTrayLabel();
		label.setFamily("Nymphalidae");
		label.setGenus("Danaus");
		label.setSpecificEpithet("plexippus");
		String json = label.toJSONString();

		BarcodeScanner dualScanner = new BarcodeScanner() {
			@Override
			public String id() {
				return "mock-dual";
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
				if (options.preferredRegion().isPresent()) {
					return List.of();
				}
				return List.of(new BarcodeDetection("ETHZ-ENT0055555", new Rectangle(720, 70, 100, 100), "mock-dual"),
						new BarcodeDetection(json, new Rectangle(320, 70, 150, 150), "mock-dual"));
			}
		};

		AtomicInteger ocrCalls = new AtomicInteger();
		OcrEngine countingOcr = new OcrEngine() {
			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public OcrResult recognize(BufferedImage region, String language) {
				ocrCalls.incrementAndGet();
				return OcrResult.success("Fallback OCR");
			}
		};

		BarcodeScannerChain dualChain = new BarcodeScannerChain(List.of(dualScanner));
		TemplateResolver resolver = new TemplateResolver(repo, new ETHZBarcode(), dualChain, null);
		SpecimenImageAnalyzer analyzer = new SpecimenImageAnalyzer(dualChain, resolver, new ETHZBarcode(), countingOcr,
				true);

		ImageAnalysisResult dualResult = analyzer
				.analyze(ImageDocument.inMemory(new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB)));
		assertEquals("MOCK_TPL", dualResult.getTemplate().getTemplateId());
		assertEquals("ETHZ-ENT0055555", dualResult.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, dualResult.getCatalogBarcodeStatus());
		assertNotNull(dualResult.getUnitTrayLabel());
		assertEquals("Nymphalidae", dualResult.getUnitTrayLabel().getFamily());
		assertEquals(0, ocrCalls.get());
		assertFalse(dualResult.getOcrText().isPresent());

		// Catalog-only scanner triggers OCR when ocrEnabled=true, skips when
		// ocrEnabled=false
		BarcodeScanner catalogOnlyScanner = new BarcodeScanner() {
			@Override
			public String id() {
				return "mock-cat-only";
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
				if (options.preferredRegion().isPresent()) {
					return List.of();
				}
				return List
						.of(new BarcodeDetection("ETHZ-ENT0055555", new Rectangle(720, 70, 100, 100), "mock-cat-only"));
			}
		};
		BarcodeScannerChain catChain = new BarcodeScannerChain(List.of(catalogOnlyScanner));
		TemplateResolver catResolver = new TemplateResolver(repo, new ETHZBarcode(), catChain, null);

		SpecimenImageAnalyzer ocrOn = new SpecimenImageAnalyzer(catChain, catResolver, new ETHZBarcode(), countingOcr,
				true);
		ImageAnalysisResult ocrRes = ocrOn
				.analyze(ImageDocument.inMemory(new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB)));
		assertEquals(1, ocrCalls.get());
		assertEquals("Fallback OCR", ocrRes.getOcrText().orElse(""));

		SpecimenImageAnalyzer ocrOff = new SpecimenImageAnalyzer(catChain, catResolver, new ETHZBarcode(), countingOcr,
				false);
		ImageAnalysisResult noOcrRes = ocrOff
				.analyze(ImageDocument.inMemory(new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB)));
		assertEquals(1, ocrCalls.get());
		assertFalse(noOcrRes.getOcrText().isPresent());

		// Empty / undecodable file produces STATUS_ERROR + diagnostic
		ImageAnalysisResult emptyRes = ocrOn.analyze(ImageDocument.of(getResourceFile(AllTests.FILE_EMPTY)));
		assertEquals(ImageAnalysisResult.STATUS_ERROR, emptyRes.getCatalogBarcodeStatus());
		assertFalse(emptyRes.getDiagnostics().isEmpty());
	}

	@Test
	public void testRealFixturesEndToEndWithoutDbOrSwing() throws Exception {
		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES, PositionTemplate.TEMPLATE_DEFAULT);

		BarcodeScannerChain chain = BarcodeScannerChain.defaultChain(props);

		// 1. IMG_007027.JPG -> MCZ-ENT00634766
		MCZENTBarcode mczMatcher = new MCZENTBarcode();
		TemplateResolver mczResolver = new TemplateResolver(new DefaultPositionTemplateRepository(), mczMatcher, chain,
				PositionTemplate.TEMPLATE_DEFAULT);
		SpecimenImageAnalyzer mczAnalyzer = new SpecimenImageAnalyzer(chain, mczResolver, mczMatcher, null, false);

		Path img7027 = getResourceFile("/IMG_007027.JPG").toPath();
		ImageAnalysisResult res7027 = mczAnalyzer.analyze(img7027);
		assertEquals("MCZ-ENT00634766", res7027.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, res7027.getCatalogBarcodeStatus());

		// 2. IMG_000060.JPG -> MCZ-ENT00050008
		Path img60 = getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG").toPath();
		ImageAnalysisResult res60 = mczAnalyzer.analyze(img60);
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, res60.getTemplate().getTemplateId());
		assertEquals("MCZ-ENT00050008", res60.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, res60.getCatalogBarcodeStatus());

		// 3. IMG_000069.JPG -> ETHZ-ENT0003497 (with empty default template)
		ETHZBarcode ethzMatcher = new ETHZBarcode();
		TemplateResolver ethzResolver = new TemplateResolver(new DefaultPositionTemplateRepository(), ethzMatcher,
				chain, "");
		SpecimenImageAnalyzer ethzAnalyzer = new SpecimenImageAnalyzer(chain, ethzResolver, ethzMatcher, null, false);

		Path img69 = getResourceFile("/IMG_000069.JPG").toPath();
		ImageAnalysisResult res69 = ethzAnalyzer.analyze(img69);
		assertEquals("ETHZ-ENT0003497", res69.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, res69.getCatalogBarcodeStatus());

		// 4. 141515321.jpg -> 141515321 (whole-image fallback with null matcher)
		TemplateResolver genericResolver = new TemplateResolver(new DefaultPositionTemplateRepository(), null, chain,
				"");
		SpecimenImageAnalyzer genericAnalyzer = new SpecimenImageAnalyzer(chain, genericResolver, null, null, false);

		Path img141 = getResourceFile(AllTests.FILE_VALID_BARCODE).toPath();
		ImageAnalysisResult res141 = genericAnalyzer.analyze(img141);
		assertEquals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS, res141.getTemplate().getTemplateId());
		assertEquals("141515321", res141.getCatalogBarcode());
		assertEquals(ImageAnalysisResult.STATUS_BARCODE_SCANNED, res141.getCatalogBarcodeStatus());
	}
}
