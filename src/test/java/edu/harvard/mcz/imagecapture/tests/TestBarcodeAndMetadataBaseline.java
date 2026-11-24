/**
 * TestBarcodeAndMetadataBaseline.java
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

import boofcv.alg.fiducial.qrcode.QrCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.harvard.mcz.imagecapture.CandidateImageFile;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.utility.GZipCompressor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Phase 1 baseline regression and characterization tests for barcode scanning,
 * template-based QR reading, and EXIF/XMP metadata extraction.
 */
public class TestBarcodeAndMetadataBaseline {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Properties savedProperties;

	@Before
	public void setUp() {
		Properties current = Singleton.getSingletonInstance().getProperties().getProperties();
		savedProperties = new Properties();
		savedProperties.putAll(current);
	}

	@After
	public void tearDown() {
		Properties current = Singleton.getSingletonInstance().getProperties().getProperties();
		current.clear();
		current.putAll(savedProperties);
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
	public void testReadBarcodeFromLocationRealFixturesAndEdgeCases() throws IOException {
		CandidateImageFile cif = new CandidateImageFile();

		BufferedImage mczImage = ImageIO.read(getResourceFile("/IMG_007027.JPG"));
		assertEquals("MCZ-ENT00634766", cif.readBarcodeFromLocation(mczImage, 3035, 135, 303, 303, false));

		BufferedImage ethzImage = ImageIO.read(getResourceFile("/IMG_000069.JPG"));
		assertEquals("ETHZ-ENT0003497", cif.readBarcodeFromLocation(ethzImage, 3380, 90, 480, 480, false));

		BufferedImage img000060 = ImageIO.read(getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG"));
		assertNotNull(img000060);
		assertEquals(2848, img000060.getWidth());
		assertEquals(4272, img000060.getHeight());
		// Verify barcode in default template catalog barcode position (2490, 90, 300,
		// 300)
		assertEquals("MCZ-ENT00050008", cif.readBarcodeFromLocation(img000060, 2490, 90, 300, 300, false));

		// Out-of-bounds coordinates (positive overflow and negative coordinates return
		// empty string)
		assertEquals("", cif.readBarcodeFromLocation(ethzImage, 3380, 90, 99999, 99999, false));
		assertEquals("", cif.readBarcodeFromLocation(ethzImage, 99999, 90, 100, 100, false));
		assertEquals("", cif.readBarcodeFromLocation(ethzImage, -10, 90, 480, 480, false));

		// Null image
		assertEquals("", cif.readBarcodeFromLocation(null, 0, 0, 100, 100, false));

		// Empty file (0 bytes -> ImageIO.read returns null)
		BufferedImage emptyImage = ImageIO.read(getResourceFile(AllTests.FILE_EMPTY));
		assertNull(emptyImage);
		assertEquals("", cif.readBarcodeFromLocation(emptyImage, 0, 0, 100, 100, false));
	}

	@Test
	public void testCandidateImageFileConstructorBehavior() throws UnreadableFileException {
		File qr1415 = getResourceFile(AllTests.FILE_VALID_BARCODE);
		CandidateImageFile valid = new CandidateImageFile(qr1415, new PositionTemplate());
		assertEquals("141515321", valid.getBarcodeText());
		assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, valid.getBarcodeStatus());

		File emptyFile = getResourceFile(AllTests.FILE_EMPTY);
		CandidateImageFile empty = new CandidateImageFile(emptyFile, new PositionTemplate());
		assertTrue(empty.isFileReadable());
		assertNull(empty.getBarcodeText());
		assertEquals(CandidateImageFile.RESULT_ERROR, empty.getBarcodeStatus());

		try {
			new CandidateImageFile(new File(AllTests.FILE_INVALID_NAME), new PositionTemplate());
			fail("Expected UnreadableFileException for nonexistent file");
		} catch (UnreadableFileException expected) {
			assertTrue(expected.getMessage() != null && !expected.getMessage().isEmpty());
		}
	}

	@Test
	public void testExifAndXmpMetadataExtraction() throws UnreadableFileException {
		CandidateImageFile mcz = new CandidateImageFile(getResourceFile("/IMG_007027.JPG"), new PositionTemplate());
		assertEquals("", mcz.getExifUserCommentText());
		assertEquals("2016-12-05", mcz.getExifDateCreated("yyyy-MM-dd"));

		CandidateImageFile ethz = new CandidateImageFile(getResourceFile("/IMG_000069.JPG"), new PositionTemplate());
		assertEquals("", ethz.getExifUserCommentText());
		assertEquals("2017-01-30", ethz.getExifDateCreated("yyyy-MM-dd"));

		CandidateImageFile img60 = new CandidateImageFile(
				getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG"), new PositionTemplate());
		assertEquals("MCZ-ENT00050008", img60.getExifUserCommentText());
		assertEquals("2009-04-22", img60.getExifDateCreated("yyyy-MM-dd"));
	}

	@Test
	public void testSyntheticSingleCatalogQrCodes() throws Exception {
		CandidateImageFile cif = new CandidateImageFile();

		BufferedImage ethzQr = renderQrImage("ETHZ-ENT0012345", 300, Color.BLACK, Color.WHITE, null);
		assertEquals("ETHZ-ENT0012345", cif.readBarcodeFromLocation(ethzQr, 0, 0, 300, 300, false));

		BufferedImage mczQr = renderQrImage("MCZ-ENT00123456", 300, Color.BLACK, Color.WHITE, null);
		assertEquals("MCZ-ENT00123456", cif.readBarcodeFromLocation(mczQr, 0, 0, 300, 300, false));
	}

	@Test
	public void testSyntheticDualQrPlainAndGzipUnitTrayLabel() throws Exception {
		// Disable external providers so this characterizes the core Java ZXing/BoofCV
		// pipeline deterministically.
		Properties props = Singleton.getSingletonInstance().getProperties().getProperties();
		props.setProperty(ImageCaptureProperties.KEY_QR_NATIVE_ENABLED, "false");
		props.setProperty(ImageCaptureProperties.KEY_QR_CHROMIUM_ENABLED, "false");

		UnitTrayLabel label = new UnitTrayLabel();
		label.setFamily("Nymphalidae");
		label.setGenus("Danaus");
		label.setSpecificEpithet("plexippus");
		label.setDrawerNumber("001.2");
		label.setCollection("ETHZ-ENT");
		String json = label.toJSONString();

		PositionTemplate template = new PositionTemplate(); // 2848x4272: barcode@(2490,90,300,300),
															// utBarcode@(1200,105,950,800)
		BufferedImage canvas = new BufferedImage(2848, 4272, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = canvas.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		BufferedImage catalogQr = renderQrImage("ETHZ-ENT0012345", 260, Color.BLACK, Color.WHITE, null);
		BufferedImage utQr = renderQrImage(json, 500, Color.BLACK, Color.WHITE, StandardCharsets.UTF_8.name());
		g.drawImage(catalogQr, 2510, 110, null);
		g.drawImage(utQr, 1300, 150, null);
		g.dispose();

		File dualFile = tempFolder.newFile("dual_qr_plain.png");
		ImageIO.write(canvas, "png", dualFile);

		CandidateImageFile cif = new CandidateImageFile(dualFile, template);
		assertEquals("ETHZ-ENT0012345", cif.getBarcodeText(template));
		assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, cif.getCatalogNumberBarcodeStatus());

		UnitTrayLabel parsed = cif.getTaxonLabelQRText(template);
		assertNotNull("Expected UnitTrayLabel parsed from plain JSON QR", parsed);
		assertEquals("Nymphalidae", parsed.getFamily());
		assertEquals("Danaus", parsed.getGenus());
		assertEquals("plexippus", parsed.getSpecificEpithet());
		assertEquals("001.2", parsed.getDrawerNumber());
		assertEquals("ETHZ-ENT", parsed.getCollection());

		// Also verify GZipCompressor + CandidateImageFile.decompressMessageIfNecessary
		byte[] compressed = GZipCompressor.compress(json);
		assertNotNull(compressed);
		assertTrue(GZipCompressor.isCompressed(compressed));
		assertEquals(json, GZipCompressor.decompress(compressed));

		QrCode qrDetection = new QrCode();
		qrDetection.corrected = compressed;
		qrDetection.message = "ignored-when-gzip";
		assertEquals(json, CandidateImageFile.decompressMessageIfNecessary(qrDetection));

		QrCode plainDetection = new QrCode();
		plainDetection.corrected = json.getBytes(StandardCharsets.UTF_8);
		plainDetection.message = json;
		assertEquals(json, CandidateImageFile.decompressMessageIfNecessary(plainDetection));
	}

	@Test
	public void testSyntheticRotatedLowContrastAndDisplacedQrCrops() throws Exception {
		CandidateImageFile cif = new CandidateImageFile();

		// 1. Slightly rotated QR (5 degrees)
		BufferedImage qr = renderQrImage("ETHZ-ENT0099999", 240, Color.BLACK, Color.WHITE, null);
		BufferedImage rotatedCanvas = new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);
		Graphics2D gRot = rotatedCanvas.createGraphics();
		gRot.setColor(Color.WHITE);
		gRot.fillRect(0, 0, 360, 360);
		gRot.rotate(Math.toRadians(5.0), 180, 180);
		gRot.drawImage(qr, 60, 60, null);
		gRot.dispose();
		assertEquals("ETHZ-ENT0099999", cif.readBarcodeFromLocation(rotatedCanvas, 0, 0, 360, 360, false));

		// 2. Low-contrast QR (dark gray on light gray)
		BufferedImage lowContrast = renderQrImage("MCZ-ENT00999999", 260, new Color(85, 85, 85),
				new Color(185, 185, 185), null);
		assertEquals("MCZ-ENT00999999", cif.readBarcodeFromLocation(lowContrast, 0, 0, 260, 260, false));

		// 3. Displaced QR crop (QR slightly shifted outside initial crop, within
		// displacement/global fallback)
		BufferedImage displacedCanvas = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics2D gDisp = displacedCanvas.createGraphics();
		gDisp.setColor(Color.WHITE);
		gDisp.fillRect(0, 0, 500, 500);
		BufferedImage smallQr = renderQrImage("ETHZ-ENT0077777", 200, Color.BLACK, Color.WHITE, null);
		gDisp.drawImage(smallQr, 130, 110, null);
		gDisp.dispose();
		assertEquals("ETHZ-ENT0077777", cif.readBarcodeFromLocation(displacedCanvas, 100, 100, 200, 200, false));
	}
}
