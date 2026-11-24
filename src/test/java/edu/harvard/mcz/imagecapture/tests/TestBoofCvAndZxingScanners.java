/**
 * TestBoofCvAndZxingScanners.java
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
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.harvard.mcz.imagecapture.scanning.BarcodeDetection;
import edu.harvard.mcz.imagecapture.scanning.BoofCvBarcodeScanner;
import edu.harvard.mcz.imagecapture.scanning.ScanOptions;
import edu.harvard.mcz.imagecapture.scanning.ZxingBarcodeScanner;
import edu.harvard.mcz.imagecapture.utility.GZipCompressor;
import georegression.struct.shapes.Polygon2D_F64;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for {@link BoofCvBarcodeScanner} and {@link ZxingBarcodeScanner}.
 */
public class TestBoofCvAndZxingScanners {

	private static BufferedImage renderQr(String content, int size) throws WriterException {
		BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size);
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
			}
		}
		return image;
	}

	@Test
	public void testBoofCvPolygonBoundsSpansRotatedVertices() {
		Polygon2D_F64 rotated = new Polygon2D_F64(4);
		rotated.set(0, 120.2, 30.1);
		rotated.set(1, 210.8, 90.4);
		rotated.set(2, 150.5, 190.9);
		rotated.set(3, 50.3, 130.7);

		Rectangle bounds = BoofCvBarcodeScanner.polygonBounds(rotated);
		assertNotNull(bounds);
		assertEquals(50, bounds.x);
		assertEquals(30, bounds.y);
		assertEquals(161, bounds.width);
		assertEquals(161, bounds.height);
		assertNull(BoofCvBarcodeScanner.polygonBounds(null));
	}

	@Test
	public void testBoofCvCenteredToleranceBoxAndPreferredRegionFiltering() throws Exception {
		Rectangle region = new Rectangle(100, 100, 100, 100);
		Rectangle box = BoofCvBarcodeScanner.centeredToleranceBox(region, 1000, 1000);
		// Region center is (150, 150); tol = 100 + 0.1*1000 = 200 -> top-left must be
		// (50, 50), not (150, 150)
		assertEquals(new Rectangle(50, 50, 200, 200), box);

		// Canvas with one QR near (100, 100) shifted by +40px so direct crop misses,
		// and another QR far outside tolerance box at (700, 700).
		BufferedImage canvas = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = canvas.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000, 1000);
		g.drawImage(renderQr("NEAR-QR", 120), 140, 140, null);
		g.drawImage(renderQr("FAR-QR", 120), 700, 700, null);
		g.dispose();

		BoofCvBarcodeScanner scanner = new BoofCvBarcodeScanner();
		// Preferred region (100, 100, 100, 100) misses part of NEAR-QR, so crop fails
		// and falls back to centeredToleranceBox
		List<BarcodeDetection> nearOnly = scanner.detect(canvas,
				ScanOptions.builder().preferredRegion(new Rectangle(100, 100, 100, 100)).quickCheck(false).build());
		assertEquals(1, nearOnly.size());
		assertEquals("NEAR-QR", nearOnly.get(0).text());

		// Preferred region far from both QRs (400, 400, 60, 60) -> tol = 160 ->
		// [350..510]
		// ignores both NEAR-QR and FAR-QR
		List<BarcodeDetection> none = scanner.detect(canvas,
				ScanOptions.builder().preferredRegion(new Rectangle(400, 400, 60, 60)).quickCheck(false).build());
		assertTrue("QR far outside tolerance box must be ignored", none.isEmpty());
	}

	@Test
	public void testBoofCvDecompressMessageIfNecessary() throws Exception {
		QrCode plain = new QrCode();
		plain.message = "ETHZ-ENT0001234";
		assertEquals("ETHZ-ENT0001234", BoofCvBarcodeScanner.decompressMessageIfNecessary(plain));

		QrCode gzipped = new QrCode();
		String payload = "{\"f\":\"Papilionidae\",\"g\":\"Papilio\"}";
		gzipped.corrected = GZipCompressor.compress(payload);
		gzipped.message = "ignored-when-gzipped";
		assertEquals(payload, BoofCvBarcodeScanner.decompressMessageIfNecessary(gzipped));

		assertEquals("", BoofCvBarcodeScanner.decompressMessageIfNecessary(null));
	}

	@Test
	public void testZxingQuickCheckDisplacementBoundsAndNoTempCropFile() throws Exception {
		Path cwdTempCrop = Paths.get("TempBarcodeCrop.png");
		Files.deleteIfExists(cwdTempCrop);

		BufferedImage canvas = new BufferedImage(320, 320, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = canvas.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 320, 320);
		g.drawImage(renderQr("ETHZ-ENT0099999", 160), 53, 53, null);
		g.dispose();

		ZxingBarcodeScanner zxing = new ZxingBarcodeScanner();
		List<BarcodeDetection> found = zxing.detect(canvas,
				ScanOptions.builder().preferredRegion(new Rectangle(50, 50, 160, 160)).quickCheck(true).build());
		assertEquals(1, found.size());
		assertEquals("ETHZ-ENT0099999", found.get(0).text());
		assertFalse("ZxingBarcodeScanner must never create TempBarcodeCrop.png", Files.exists(cwdTempCrop));

		assertTrue(zxing.detect(canvas, ScanOptions.builder().preferredRegion(new Rectangle(-5, 0, 50, 50)).build())
				.isEmpty());
		assertTrue(
				zxing.detect(canvas, ScanOptions.builder().preferredRegion(new Rectangle(250, 250, 100, 100)).build())
						.isEmpty());
		assertFalse("ZxingBarcodeScanner must never create TempBarcodeCrop.png", Files.exists(cwdTempCrop));
	}
}
