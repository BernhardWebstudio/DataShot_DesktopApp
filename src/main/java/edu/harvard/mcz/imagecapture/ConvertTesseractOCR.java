/**
 * ConvertTesseractOCR.java
 * edu.harvard.mcz.imagecapture
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
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.exceptions.OCRReadException;
import edu.harvard.mcz.imagecapture.interfaces.OCR;
import edu.harvard.mcz.imagecapture.ocr.OcrResult;
import edu.harvard.mcz.imagecapture.ocr.TesseractCliOcrEngine;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Legacy {@link OCR} wrapper that delegates buffered-image OCR to
 * {@link TesseractCliOcrEngine} without ImageMagick conversion or
 * working-directory temp files.
 * <p>
 * Usage:
 *
 * <pre>
 * ConvertTesseractOCR ocr = new ConvertTesseractOCR(aBufferedImage);
 * String result = ocr.getOCRText();
 * </pre>
 */
public class ConvertTesseractOCR implements OCR {

	private static final Logger log = LoggerFactory.getLogger(ConvertTesseractOCR.class);
	private final BufferedImage subimage;
	private final TesseractCliOcrEngine ocrEngine;

	public ConvertTesseractOCR(BufferedImage anImageToOCR) {
		this(anImageToOCR, new TesseractCliOcrEngine());
	}

	ConvertTesseractOCR(BufferedImage anImageToOCR, TesseractCliOcrEngine ocrEngine) {
		this.subimage = anImageToOCR;
		this.ocrEngine = ocrEngine;
	}

	/**
	 * Command line test for this class.
	 *
	 * @param args
	 *            will take first argument as path and name of file, will extract a
	 *            portion of this image to test using the default position template.
	 * @see edu.harvard.mcz.imagecapture.PositionTemplate
	 */
	public static void main(String[] args) {
		String target = "/home/mole/stuff/MCZ/mcz/insects/testImages/base/photo_plate_test_good_4_template.JPG";
		if (args.length == 1) {
			target = args[0];
		}
		File targetFile = new File(target);
		PositionTemplate defaultTemplate = new PositionTemplate();
		try {
			BufferedImage imagefile = ImageIO.read(targetFile);
			int x = defaultTemplate.getTextPosition().width;
			int y = defaultTemplate.getTextPosition().height;
			int w = defaultTemplate.getTextSize().width;
			int h = defaultTemplate.getTextSize().height;

			ConvertTesseractOCR converter = new ConvertTesseractOCR(imagefile.getSubimage(x, y, w, h));

			try {
				String output = converter.getOCRText();
				System.out.println(output);
			} catch (OCRReadException e) {
				System.out.println("OCR Failed." + e.getMessage());
			}
		} catch (IOException e1) {
			System.out.println("Couldn't read imagefile " + target + "." + e1.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.harvard.mcz.imagecapture.interfaces.OCR#getOCRText()
	 */
	@Override
	public String getOCRText() throws OCRReadException {
		if (subimage == null) {
			throw new OCRReadException("Image to OCR is null.");
		}
		OcrResult result = ocrEngine.recognize(subimage, TesseractCliOcrEngine.DEFAULT_LANGUAGE);
		if (!result.isSuccess()) {
			log.debug("OCR failed: {}", result.getErrorMessage());
			throw new OCRReadException(result.getErrorMessage());
		}
		return result.getText();
	}
}
