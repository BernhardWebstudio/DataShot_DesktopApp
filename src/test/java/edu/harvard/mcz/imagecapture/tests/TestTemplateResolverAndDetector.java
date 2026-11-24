/**
 * TestTemplateResolverAndDetector.java
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

import edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetector;
import edu.harvard.mcz.imagecapture.ETHZBarcode;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.MCZENTBarcode;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.scanning.BarcodeDetection;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScanner;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScannerChain;
import edu.harvard.mcz.imagecapture.scanning.ExternalProcessRunner;
import edu.harvard.mcz.imagecapture.scanning.PositionTemplateRepository;
import edu.harvard.mcz.imagecapture.scanning.ScanOptions;
import edu.harvard.mcz.imagecapture.scanning.TemplateResolver;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link TemplateResolver} and
 * {@link ConfiguredBarcodePositionTemplateDetector}.
 */
public class TestTemplateResolverAndDetector {

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

	private static PositionTemplate makeTemplate(String id, int imgW, int imgH, int bcX, int bcY, int bcW, int bcH) {
		PositionTemplate t = new PositionTemplate();
		t.setTemplateId(id);
		t.setTemplateName(id);
		t.setImageSize(new Dimension(imgW, imgH));
		t.setBarcodePosition(new Dimension(bcX, bcY));
		t.setBarcodeSize(new Dimension(bcW, bcH));
		return t;
	}

	private static PositionTemplateRepository inMemoryRepo(List<PositionTemplate> templates) {
		return new PositionTemplateRepository() {
			@Override
			public List<PositionTemplate> findAll() {
				return templates;
			}

			@Override
			public Optional<PositionTemplate> findById(String templateId) {
				return templates.stream().filter(t -> t.getTemplateId().equals(templateId)).findFirst();
			}
		};
	}

	@Test
	public void testScanOnceBoundingBoxMatchingResolvesInOneInvocation() {
		PositionTemplate t1 = makeTemplate("T_LEFT", 1000, 1000, 50, 50, 100, 100);
		PositionTemplate t2 = makeTemplate("T_RIGHT", 1000, 1000, 800, 50, 120, 120);
		PositionTemplateRepository repo = inMemoryRepo(List.of(t1, t2));

		AtomicInteger calls = new AtomicInteger();
		BarcodeScanner fakeScanner = new BarcodeScanner() {
			@Override
			public String id() {
				return "fake";
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
				calls.incrementAndGet();
				return List.of(new BarcodeDetection("ETHZ-ENT0001111", new Rectangle(810, 60, 80, 80), "fake"));
			}
		};

		TemplateResolver resolver = new TemplateResolver(repo, new ETHZBarcode(),
				new BarcodeScannerChain(List.of(fakeScanner)), null);

		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		PositionTemplate resolved = resolver.resolveTemplate(img, null, true);
		assertEquals("T_RIGHT", resolved.getTemplateId());
		assertEquals("Expected scan-once bounding-box match in 1 scanner invocation", 1, calls.get());
	}

	@Test
	public void testPerTemplateRegionFallbackWhenBoundsAbsentRespectsQuickCheck() {
		PositionTemplate t1 = makeTemplate("T_FIRST", 1000, 1000, 50, 50, 100, 100);
		PositionTemplate t2 = makeTemplate("T_SECOND", 1000, 1000, 700, 100, 150, 150);
		PositionTemplateRepository repo = inMemoryRepo(List.of(t1, t2));

		List<ScanOptions> recordedOptions = new ArrayList<>();
		BarcodeScanner fakeScanner = new BarcodeScanner() {
			@Override
			public String id() {
				return "fake-no-bounds";
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
				recordedOptions.add(options);
				if (options.preferredRegion().isEmpty()) {
					// Full-image detection without bounding box -> forces region fallback
					return List.of(new BarcodeDetection("ETHZ-ENT0002222", (Rectangle) null, "fake-no-bounds"));
				}
				Rectangle region = options.preferredRegion().get();
				if (region.x == 700) {
					return List.of(new BarcodeDetection("ETHZ-ENT0002222", region, "fake-no-bounds"));
				}
				return List.of();
			}
		};

		TemplateResolver resolver = new TemplateResolver(repo, new ETHZBarcode(),
				new BarcodeScannerChain(List.of(fakeScanner)), null);

		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		PositionTemplate resolved = resolver.resolveTemplate(img, null, true);
		assertEquals("T_SECOND", resolved.getTemplateId());
		assertEquals(3, recordedOptions.size());
		assertTrue("Region fallback must respect quickCheck=true", recordedOptions.get(1).quickCheck());
		assertTrue("Region fallback must respect quickCheck=true", recordedOptions.get(2).quickCheck());
	}

	@Test
	public void testNonInternedDefaultTemplateAndExplicitConfiguredDefault() {
		PositionTemplate explicit = makeTemplate("EXPLICIT_TPL", 1000, 1000, 50, 50, 100, 100);
		PositionTemplateRepository repo = inMemoryRepo(List.of(explicit));
		BarcodeScanner neverCalled = new BarcodeScanner() {
			@Override
			public String id() {
				return "never";
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
				fail("Scanner should not be called when explicit default template is configured");
				return List.of();
			}
		};

		TemplateResolver defaultResolver = new TemplateResolver(repo, new ETHZBarcode(),
				new BarcodeScannerChain(List.of(neverCalled)), new String(PositionTemplate.TEMPLATE_DEFAULT));
		assertFalse("Non-interned 'Default template' must not be treated as explicit default",
				defaultResolver.hasExplicitDefaultTemplate());

		TemplateResolver explicitResolver = new TemplateResolver(repo, new ETHZBarcode(),
				new BarcodeScannerChain(List.of(neverCalled)), "EXPLICIT_TPL");
		assertTrue(explicitResolver.hasExplicitDefaultTemplate());
		PositionTemplate resolved = explicitResolver
				.resolveTemplate(new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB), null, true);
		assertEquals("EXPLICIT_TPL", resolved.getTemplateId());
	}

	@Test
	public void testConfiguredDetectorWithNullScannableFileDoesNotThrowNpe() throws Exception {
		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES, PositionTemplate.TEMPLATE_DEFAULT);
		props.setProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false");
		Singleton.getSingletonInstance().setBarcodeMatcher(new MCZENTBarcode());
		Singleton.getSingletonInstance().setBarcodeBuilder(new MCZENTBarcode());

		ConfiguredBarcodePositionTemplateDetector detector = new ConfiguredBarcodePositionTemplateDetector();

		File img60 = getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG");
		assertEquals(PositionTemplate.TEMPLATE_DEFAULT, detector.detectTemplateForImage(img60));

		File empty = getResourceFile(AllTests.FILE_EMPTY);
		assertEquals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS, detector.detectTemplateForImage(empty));
	}
}
