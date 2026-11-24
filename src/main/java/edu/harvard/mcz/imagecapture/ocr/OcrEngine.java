/**
 * OcrEngine.java
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

import java.awt.image.BufferedImage;

/**
 * Abstraction for optical character recognition engines operating on image
 * regions.
 */
public interface OcrEngine {

	/**
	 * @return {@code true} if the OCR engine is available in the current
	 *         environment
	 */
	boolean isAvailable();

	/**
	 * Extracts text from the supplied image {@code region}.
	 *
	 * @param region
	 *            cropped label image
	 * @param language
	 *            Tesseract language code (e.g. {@code "lepid"} or {@code "eng"})
	 * @return immutable {@link OcrResult}
	 */
	OcrResult recognize(BufferedImage region, String language);
}
