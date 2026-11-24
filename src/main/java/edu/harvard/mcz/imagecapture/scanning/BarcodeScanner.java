/**
 * BarcodeScanner.java
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

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Strategy interface for barcode/QR detection engines.
 */
public interface BarcodeScanner {

	/**
	 * @return stable identifier for this scanner engine
	 */
	String id();

	/**
	 * @return {@code true} if this scanner is enabled and ready to run in the
	 *         current environment
	 */
	boolean isAvailable();

	/**
	 * Scans {@code image} for barcodes according to {@code options}.
	 *
	 * @param image
	 *            decoded image (may be {@code null})
	 * @param options
	 *            scan options (uses {@link ScanOptions#defaults()} if {@code null})
	 * @return list of detections (never {@code null})
	 */
	List<BarcodeDetection> detect(BufferedImage image, ScanOptions options);
}
