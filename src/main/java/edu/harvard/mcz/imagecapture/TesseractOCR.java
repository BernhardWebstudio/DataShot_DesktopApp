/**
 * TesseractOCR.java
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
import java.io.File;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses {@link TesseractCliOcrEngine} to invoke tesseract and obtain OCR text
 * from an image file without working-directory temp files or blocking stream
 * reads.
 * <p>
 * Usage:
 *
 * <pre>
 * String output = null;
 * try {
 * 	TesseractOCR t = new TesseractOCR();
 * 	t.setTarget("filenameToOCR.tif");
 * 	output = t.getOCRText();
 * } catch (OCRReadException e) {
 * 	// handle exception
 * }
 * </pre>
 */
public class TesseractOCR implements OCR {

	private static final Logger log = LoggerFactory.getLogger(TesseractOCR.class);
	private final TesseractCliOcrEngine ocrEngine;
	private String target = "/home/mole/stuff/MCZ/mcz/insects/testImages/text.tif";
	private String language = TesseractCliOcrEngine.DEFAULT_LANGUAGE; // Lepidoptera training set

	public TesseractOCR() {
		this(new TesseractCliOcrEngine());
	}

	TesseractOCR(TesseractCliOcrEngine ocrEngine) {
		this.ocrEngine = ocrEngine;
	}

	/**
	 * Run TesseractOCR from the command line to OCR a single file, intended as a
	 * test of installation. Displays the content of the tesseract output file to
	 * standard output. Could be used as a shell wrapper to run tesseract and put
	 * the results on standard output to be piped elsewhere.
	 *
	 * @param args
	 *            name of the image file to try to OCR.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java TesseractOCR <inputfilename> [<lang>]");
			System.exit(1);
		} else {
			TesseractOCR o = new TesseractOCR();
			try {
				if (args.length > 1) {
					o.setLang(args[1]);
				}
				o.setTarget(args[0]);
				System.out.println(o.getOCRText());
				System.exit(0);
			} catch (OCRReadException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				System.exit(2);
			}
		}
	}

	/**
	 * Sets the name of the image file from which tesseract is to try to extract
	 * text.
	 *
	 * @param aTargetFile
	 *            filename, including path, of the file that is to be OCRed.
	 * @throws OCRReadException
	 *             if the file cannot be read.
	 */
	public void setTarget(String aTargetFile) throws OCRReadException {
		if (aTargetFile != null && new File(aTargetFile).canRead()) {
			target = aTargetFile;
		} else {
			throw new OCRReadException("Can't read file: " + aTargetFile);
		}
	}

	/**
	 * Set the language to use with Tesseract, use "eng" for english, passed to
	 * tesseract as "-l lang". Represents the leading string for the tesseract
	 * training files (e.g. eng for eng.normproto, eng.word-dawg, etc). Default is
	 * "lepid" for custom scientific name/author training files.
	 *
	 * @param lang
	 *            Tesseract language code
	 */
	public void setLang(String lang) {
		language = lang;
	}

	public String getOCRText(String lang) throws OCRReadException {
		setLang(lang);
		return getOCRText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.harvard.mcz.imagecapture.interfaces.OCR#getOCRText()
	 */
	@Override
	public String getOCRText() throws OCRReadException {
		OcrResult result = ocrEngine.recognizeFile(Paths.get(target), language);
		if (!result.isSuccess()) {
			log.debug("TesseractOCR failed: {}", result.getErrorMessage());
			throw new OCRReadException(result.getErrorMessage());
		}
		return result.getText();
	}
}
