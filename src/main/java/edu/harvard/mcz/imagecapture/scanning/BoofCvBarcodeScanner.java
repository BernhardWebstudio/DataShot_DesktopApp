/**
 * BoofCvBarcodeScanner.java
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

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import edu.harvard.mcz.imagecapture.utility.GZipCompressor;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BarcodeScanner} backed by BoofCV with accurate 4-vertex polygon
 * bounding boxes, centered tolerance filtering, and GZip QR payload
 * decompression.
 */
public class BoofCvBarcodeScanner implements BarcodeScanner {

	private static final Logger log = LoggerFactory.getLogger(BoofCvBarcodeScanner.class);

	@Override
	public String id() {
		return "boofcv";
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
		if (image == null) {
			return List.of();
		}
		ScanOptions effective = options == null ? ScanOptions.defaults() : options;
		Optional<Rectangle> preferred = effective.preferredRegion();
		if (preferred.isEmpty()) {
			return detectAll(image);
		}

		Rectangle region = preferred.get();
		Optional<BufferedImage> cropOpt = ImagePreprocessor.copySubimage(image, region);
		if (cropOpt.isPresent()) {
			List<BarcodeDetection> cropDetections = detectAll(cropOpt.get());
			if (!cropDetections.isEmpty()) {
				List<BarcodeDetection> translated = new ArrayList<>(cropDetections.size());
				for (BarcodeDetection d : cropDetections) {
					Rectangle shifted = d.bounds()
							.map(b -> new Rectangle(region.x + b.x, region.y + b.y, b.width, b.height)).orElse(region);
					translated.add(new BarcodeDetection(d.text(), shifted, id()));
				}
				return translated;
			}
		}

		if (effective.quickCheck()) {
			return List.of();
		}

		Rectangle toleranceBox = centeredToleranceBox(region, image.getWidth(), image.getHeight());
		List<BarcodeDetection> fullDetections = detectAll(image);
		List<BarcodeDetection> matching = new ArrayList<>();
		for (BarcodeDetection detection : fullDetections) {
			if (detection.bounds().map(b -> b.intersects(toleranceBox)).orElse(false)) {
				matching.add(detection);
			}
		}
		return matching;
	}

	/**
	 * Decompresses GZip-encoded QR code payloads if necessary, otherwise returns
	 * the plain decoded message.
	 *
	 * @param detection
	 *            BoofCV QR detection
	 * @return decoded string (empty string if decompression fails or payload is
	 *         null)
	 */
	public static String decompressMessageIfNecessary(QrCode detection) {
		if (detection == null) {
			return "";
		}
		byte[] dataBytes = detection.corrected;
		if (dataBytes != null && GZipCompressor.isCompressed(dataBytes)) {
			try {
				return GZipCompressor.decompress(dataBytes);
			} catch (IOException e) {
				log.error("GZip decompression failed: {}", e.getMessage());
				return "";
			}
		}
		return detection.message == null ? "" : detection.message;
	}

	/**
	 * Computes an axis-aligned bounding box spanning all vertices of a BoofCV
	 * {@link Polygon2D_F64} (accurate for rotated QR codes).
	 *
	 * @param polygon
	 *            detection polygon
	 * @return bounding box, or {@code null} if the polygon has no vertices
	 */
	public static Rectangle polygonBounds(Polygon2D_F64 polygon) {
		if (polygon == null || polygon.size() == 0) {
			return null;
		}
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < polygon.size(); i++) {
			Point2D_F64 vertex = polygon.get(i);
			minX = Math.min(minX, vertex.x);
			minY = Math.min(minY, vertex.y);
			maxX = Math.max(maxX, vertex.x);
			maxY = Math.max(maxY, vertex.y);
		}
		int x = (int) Math.floor(minX);
		int y = (int) Math.floor(minY);
		int width = Math.max(1, (int) Math.ceil(maxX - minX));
		int height = Math.max(1, (int) Math.ceil(maxY - minY));
		return new Rectangle(x, y, width, height);
	}

	public static Rectangle centeredToleranceBox(Rectangle region, int imageWidth, int imageHeight) {
		double centerX = region.x + region.width / 2.0;
		double centerY = region.y + region.height / 2.0;
		int tol = Math.max(1,
				(int) Math.round(Math.max(region.width, region.height) + 0.1 * Math.min(imageWidth, imageHeight)));
		int x = (int) Math.round(centerX - tol / 2.0);
		int y = (int) Math.round(centerY - tol / 2.0);
		return new Rectangle(x, y, tol, tol);
	}

	private List<BarcodeDetection> detectAll(BufferedImage image) {
		GrayU8 gray = ConvertBufferedImage.convertFrom(image, new GrayU8(1, 1));
		QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null, GrayU8.class);
		detector.process(gray);
		List<QrCode> rawDetections = detector.getDetections();
		if (rawDetections == null || rawDetections.isEmpty()) {
			return List.of();
		}
		List<BarcodeDetection> results = new ArrayList<>(rawDetections.size());
		for (QrCode qr : rawDetections) {
			String text = decompressMessageIfNecessary(qr);
			if (!text.isEmpty()) {
				results.add(new BarcodeDetection(text, polygonBounds(qr.bounds), id()));
			}
		}
		return results;
	}
}
