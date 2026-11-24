/**
 * CandidateImageFile.java
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

import boofcv.alg.fiducial.qrcode.QrCode;
import edu.harvard.mcz.imagecapture.data.BulkMedia;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.ocr.OcrEngine;
import edu.harvard.mcz.imagecapture.ocr.OcrResult;
import edu.harvard.mcz.imagecapture.ocr.TesseractCliOcrEngine;
import edu.harvard.mcz.imagecapture.scanning.BarcodeDetection;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScannerChain;
import edu.harvard.mcz.imagecapture.scanning.BoofCvBarcodeScanner;
import edu.harvard.mcz.imagecapture.scanning.ImageAnalysisResult;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.ImageMetadata;
import edu.harvard.mcz.imagecapture.scanning.ImagePreprocessor;
import edu.harvard.mcz.imagecapture.scanning.ScanOptions;
import edu.harvard.mcz.imagecapture.scanning.SpecimenImageAnalyzer;
import edu.harvard.mcz.imagecapture.scanning.ZxingBarcodeScanner;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade over {@link ImageDocument}, {@link SpecimenImageAnalyzer},
 * {@link BarcodeScannerChain}, {@link OcrEngine}, and {@link BulkMediaExporter}
 * for extracting barcodes, unit-tray labels, EXIF/XMP metadata, and optional
 * OCR text from specimen image files.
 */
public class CandidateImageFile {

	public static final int RESULT_NOT_CHECKED = 0;
	public static final int RESULT_BARCODE_SCANNED = 1;
	public static final int RESULT_ERROR = 2;

	/**
	 * Initial pixel width to try rescaling barcode areas to.
	 */
	public static final double INITIAL_SCALING_WIDTH = ZxingBarcodeScanner.INITIAL_SCALING_WIDTH;

	private static final Logger log = LoggerFactory.getLogger(CandidateImageFile.class);

	private final SpecimenImageAnalyzer analyzer;
	private final OcrEngine ocrEngine;

	private File candidateFile;
	private ImageDocument imageDocument;
	private int barcodeStatus;
	private int catalogNumberBarcodeStatus;
	private String catalogNumberBarcodeText = null;
	private String exifCommentText = null;
	private int unitTrayTaxonLabelTextStatus;
	private String labelText = null;
	private UnitTrayLabel unitTrayLabel;
	private Date dateCreated = null;
	private PositionTemplate templateUsed = null;

	/**
	 * Constructor which detects the template to be used with the candidate image
	 * file and runs single-pass analysis via {@link SpecimenImageAnalyzer}.
	 *
	 * @param aFile
	 *            the image file to analyze
	 * @throws UnreadableFileException
	 *             if {@code aFile} cannot be read
	 */
	public CandidateImageFile(File aFile) throws UnreadableFileException {
		this(aFile, null, null, true);
	}

	/**
	 * Constructor using a caller-supplied {@link PositionTemplate}.
	 *
	 * @param aFile
	 *            the image file that may contain a barcode or text
	 * @param aTemplate
	 *            the {@link PositionTemplate} specifying where barcode or OCR text
	 *            may occur
	 * @throws UnreadableFileException
	 *             if {@code aFile} cannot be read
	 */
	public CandidateImageFile(File aFile, PositionTemplate aTemplate) throws UnreadableFileException {
		this.analyzer = null;
		this.ocrEngine = null;
		setFile(aFile, aTemplate);
	}

	/**
	 * Constructor with no parameters for accessing convenience and image-level
	 * methods. Follow with {@link #setFile(File, PositionTemplate)} before calling
	 * file-backed methods.
	 */
	public CandidateImageFile() {
		this.analyzer = null;
		this.ocrEngine = null;
	}

	/**
	 * Package-private constructor for tests injecting custom analysis or OCR
	 * collaborators.
	 */
	CandidateImageFile(File aFile, SpecimenImageAnalyzer analyzer, OcrEngine ocrEngine) throws UnreadableFileException {
		this(aFile, analyzer, ocrEngine, true);
	}

	private CandidateImageFile(File aFile, SpecimenImageAnalyzer analyzer, OcrEngine ocrEngine, boolean autoAnalyze)
			throws UnreadableFileException {
		this.analyzer = analyzer;
		this.ocrEngine = ocrEngine;
		if (autoAnalyze) {
			init();
			this.candidateFile = aFile;
			this.imageDocument = ImageDocument.of(aFile);
			this.imageDocument.ensureReadable();
			SpecimenImageAnalyzer effectiveAnalyzer = analyzer != null
					? analyzer
					: SpecimenImageAnalyzer.fromSingleton();
			ImageAnalysisResult result = effectiveAnalyzer.analyze(this.imageDocument);
			applyAnalysisResult(result);
		}
	}

	private void applyAnalysisResult(ImageAnalysisResult result) {
		this.templateUsed = result.getTemplate();
		ImageMetadata metadata = result.getMetadata();
		this.exifCommentText = metadata.effectiveUserComment();
		this.dateCreated = metadata.getDateCreated();
		this.catalogNumberBarcodeStatus = result.getCatalogBarcodeStatus();
		this.catalogNumberBarcodeText = result.getCatalogBarcode();
		this.unitTrayLabel = result.getUnitTrayLabel();
		this.unitTrayTaxonLabelTextStatus = result.getUnitTrayLabelStatus();
		this.labelText = result.getRawLabelText();
		if (this.labelText == null && result.getOcrText().isPresent()) {
			this.labelText = result.getOcrText().get();
		}
		if (result.getUnitTrayLabelStatus() == RESULT_BARCODE_SCANNED
				|| result.getCatalogBarcodeStatus() == RESULT_BARCODE_SCANNED) {
			this.barcodeStatus = RESULT_BARCODE_SCANNED;
		} else if (result.getUnitTrayLabelStatus() == RESULT_ERROR
				|| result.getCatalogBarcodeStatus() == RESULT_ERROR) {
			this.barcodeStatus = RESULT_ERROR;
		} else {
			this.barcodeStatus = RESULT_NOT_CHECKED;
		}
	}

	public String getLabelText() {
		return this.labelText;
	}

	/**
	 * Self standing starting point to extract barcodes from image files.
	 *
	 * @param args
	 *            command line arguments, run with none or -h for help.
	 */
	public static void main(String[] args) {
		BulkMediaExporter.main(args);
	}

	/**
	 * Checks image height, width and exif metadata height width and orientation,
	 * writes to debug log. Does nothing if logging level does not include debug.
	 *
	 * @param aFile
	 *            to check
	 */
	public static void debugCheckHeightWidth(File aFile) {
		BulkMediaExporter.debugCheckHeightWidth(aFile);
	}

	protected static void showBulkMediaGUI() {
		BulkMediaExporter.showBulkMediaGUI();
	}

	protected static String parseOneFile(String filename) {
		return BulkMediaExporter.parseOneFile(filename);
	}

	/**
	 * Produce a {@link BulkMedia} record suitable for loading into the MCZbase bulk
	 * media bulkloader from an image file.
	 *
	 * @param filename
	 *            the filename to load as bulk media
	 * @return {@link BulkMedia} object for that file
	 */
	public static BulkMedia parseOneFileToBulkMedia(String filename) {
		return BulkMediaExporter.parseOneFileToBulkMedia(filename);
	}

	/**
	 * Decompress using {@link edu.harvard.mcz.imagecapture.utility.GZipCompressor}
	 * in case a {@link QrCode} contains compressed data.
	 *
	 * @param detection
	 *            BoofCV QR detection
	 * @return decompressed or plain QR message
	 */
	public static String decompressMessageIfNecessary(QrCode detection) {
		return BoofCvBarcodeScanner.decompressMessageIfNecessary(detection);
	}

	/**
	 * Convenience method to check an image for a barcode. Does not set any instance
	 * variables of {@code CandidateImageFile}, and does not behave precisely as the
	 * {@link #getBarcodeText()} methods. Result state is not available from
	 * {@link #getBarcodeStatus()} and both errors and the absence of a barcode in
	 * the image result in an empty string being returned.
	 *
	 * @param image
	 *            The {@link BufferedImage} to check for a barcode.
	 * @param positionTemplate
	 *            The position template specifying where in the image to check for
	 *            the barcode; if
	 *            {@link PositionTemplate#TEMPLATE_NO_COMPONENT_PARTS}, the entire
	 *            image is checked, otherwise only the catalog barcode region is
	 *            checked.
	 * @param quickCheck
	 *            whether to skip exhaustive retry transformations
	 * @return the text of the barcode found, or an empty string
	 */
	public String getBarcodeTextFromImage(BufferedImage image, PositionTemplate positionTemplate, boolean quickCheck) {
		if (image == null || positionTemplate == null) {
			return "";
		}
		log.debug("Position template name: {}", positionTemplate.getName());
		if (PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(positionTemplate.getTemplateId())) {
			ScanOptions options = ScanOptions.fromProperties(readSingletonProperties()).toBuilder()
					.quickCheck(quickCheck).build();
			List<BarcodeDetection> detections = BarcodeScannerChain.fromSingleton().scanAll(image, options);
			String selected = selectFullImageDetectionText(detections);
			return selected != null ? selected : "";
		}
		if (positionTemplate.getBarcodeULPosition() != null && positionTemplate.getBarcodeSize() != null
				&& image.getWidth() > positionTemplate.getBarcodeULPosition().width) {
			int left = positionTemplate.getBarcodeULPosition().width;
			int top = positionTemplate.getBarcodeULPosition().height;
			int width = positionTemplate.getBarcodeSize().width;
			int height = positionTemplate.getBarcodeSize().height;
			return readBarcodeFromLocation(image, left, top, width, height, quickCheck);
		}
		return "";
	}

	/**
	 * Convenience method to check an image for a unit-tray taxon label barcode.
	 * Does not set any instance variables of {@code CandidateImageFile}.
	 *
	 * @param image
	 *            The {@link BufferedImage} to check for a barcode.
	 * @param positionTemplate
	 *            The position template specifying where in the image to check for
	 *            the UnitTrayLabel barcode; if
	 *            {@link PositionTemplate#TEMPLATE_NO_COMPONENT_PARTS}, the entire
	 *            image is checked.
	 * @return the text of the barcode found in the UnitTrayLabel portion, or an
	 *         empty string
	 */
	public String getBarcodeUnitTrayTextFromImage(BufferedImage image, PositionTemplate positionTemplate) {
		if (image == null || positionTemplate == null) {
			return "";
		}
		if (PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(positionTemplate.getTemplateId())) {
			ScanOptions options = ScanOptions.fromProperties(readSingletonProperties());
			List<BarcodeDetection> detections = BarcodeScannerChain.fromSingleton().scanAll(image, options);
			Optional<UnitTrayMatch> unitTrayMatch = selectUnitTrayDetection(detections);
			if (unitTrayMatch.isPresent()) {
				return unitTrayMatch.get().text();
			}
			return detections.isEmpty() ? "" : detections.get(0).text();
		}
		if (positionTemplate.getUtBarcodePosition() != null && positionTemplate.getUtBarcodeSize() != null
				&& image.getWidth() > positionTemplate.getUtBarcodePosition().width) {
			int left = positionTemplate.getUtBarcodePosition().width;
			int top = positionTemplate.getUtBarcodePosition().height;
			int width = positionTemplate.getUtBarcodeSize().width;
			int height = positionTemplate.getUtBarcodeSize().height;
			return readBarcodeFromLocation(image, left, top, width, height, false);
		}
		return "";
	}

	/**
	 * Read the barcode content from a portion of an image using in-process ZXing
	 * and BoofCV scanners without writing temporary debug files to the working
	 * directory.
	 *
	 * @param image
	 *            to crop a portion out of to detect a barcode
	 * @param left
	 *            x coordinate in image of leftmost pixels to decode
	 * @param top
	 *            y coordinate in image of topmost pixels to decode
	 * @param width
	 *            in pixels of the area to decode
	 * @param height
	 *            in pixels of the area to decode
	 * @param quickCheck
	 *            whether to skip exhaustive retry transformations
	 * @return string content of barcode found, or an empty string
	 */
	public String readBarcodeFromLocation(BufferedImage image, int left, int top, int width, int height,
			boolean quickCheck) {
		if (image == null || left < 0 || top < 0 || width <= 0 || height <= 0 || (long) left + width > image.getWidth()
				|| (long) top + height > image.getHeight()) {
			return "";
		}
		Rectangle region = new Rectangle(left, top, width, height);
		ScanOptions options = ScanOptions.fromProperties(readSingletonProperties()).toBuilder().preferredRegion(region)
				.quickCheck(quickCheck).build();
		BarcodeScannerChain chain = new BarcodeScannerChain(
				List.of(new ZxingBarcodeScanner(), new BoofCvBarcodeScanner()));
		List<BarcodeDetection> detections = chain.scanRegion(image, region, options);
		if (detections.isEmpty()) {
			return "";
		}
		return detections.get(0).text();
	}

	/**
	 * Test to see if the file provided in the constructor or {@link #setFile}
	 * method is readable.
	 *
	 * @return {@code true} if the file is readable
	 * @throws UnreadableFileException
	 *             if file is null, does not exist, or cannot be read
	 */
	public boolean isFileReadable() throws UnreadableFileException {
		if (imageDocument == null) {
			throw new UnreadableFileException("No such file. CandidateImageFile given null for a filename.");
		}
		imageDocument.ensureReadable();
		return true;
	}

	private void init() {
		barcodeStatus = RESULT_NOT_CHECKED;
		catalogNumberBarcodeStatus = RESULT_NOT_CHECKED;
		catalogNumberBarcodeText = null;
		exifCommentText = null;
		unitTrayTaxonLabelTextStatus = RESULT_NOT_CHECKED;
		unitTrayLabel = null;
		labelText = null;
		dateCreated = null;
		templateUsed = null;
	}

	/**
	 * Change the image file and position template.
	 *
	 * @param aFile
	 *            image file
	 * @param aTemplate
	 *            position template
	 * @throws UnreadableFileException
	 *             if the file cannot be read
	 */
	public void setFile(File aFile, PositionTemplate aTemplate) throws UnreadableFileException {
		candidateFile = aFile;
		imageDocument = ImageDocument.of(aFile);
		imageDocument.ensureReadable();
		init();

		getExifUserCommentText();
		if (aTemplate != null && !PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(aTemplate.getTemplateId())) {
			if (getTaxonLabelQRText(aTemplate) == null && isOcrEnabled()) {
				try {
					getTaxonLabelOCRText(aTemplate);
				} catch (OCRReadException e) {
					log.error("Unable to OCR file: {} {}", candidateFile != null ? candidateFile.getName() : "",
							e.getMessage());
				}
			}
		}
		templateUsed = aTemplate;
	}

	/**
	 * Obtain the {@link File} for this candidate image file.
	 *
	 * @return the candidateFile
	 */
	public File getFile() {
		return candidateFile;
	}

	/**
	 * Obtain the cached {@link ImageDocument} backing this candidate image file.
	 *
	 * @return the imageDocument, or {@code null} if not initialized
	 */
	public ImageDocument getImageDocument() {
		return imageDocument;
	}

	/**
	 * If the image contains a taxon label text encoded in a QRCode barcode in the
	 * position specified for the Taxon/UnitTrayLabel Barcode by the
	 * {@link PositionTemplate}, return that text as a {@link UnitTrayLabel} object.
	 *
	 * @param positionTemplate
	 *            the template specifying the location of the barcode
	 * @return {@code null} or a {@link UnitTrayLabel} containing the parsed text
	 */
	public UnitTrayLabel getTaxonLabelQRText(PositionTemplate positionTemplate) {
		if (unitTrayLabel != null && unitTrayTaxonLabelTextStatus == RESULT_BARCODE_SCANNED && labelText != null
				&& !labelText.isEmpty()) {
			return unitTrayLabel;
		}
		if (positionTemplate == null) {
			barcodeStatus = RESULT_ERROR;
			unitTrayTaxonLabelTextStatus = RESULT_ERROR;
			labelText = null;
			return null;
		}

		UnitTrayLabel resultLabel = null;
		String rawJson = null;
		barcodeStatus = RESULT_ERROR;
		BufferedImage image = loadDocumentImage();

		if (image != null) {
			ScanOptions baseOptions = ScanOptions.fromProperties(readSingletonProperties());
			BarcodeScannerChain chain = BarcodeScannerChain.fromSingleton();
			List<BarcodeDetection> detections;
			if (PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(positionTemplate.getTemplateId())) {
				detections = chain.scanAll(image, baseOptions);
			} else if (positionTemplate.getUtBarcodePosition() != null && positionTemplate.getUtBarcodeSize() != null
					&& positionTemplate.getImageSize() != null
					&& image.getWidth() >= positionTemplate.getUtBarcodePosition().width
					&& image.getWidth() == Math.round(positionTemplate.getImageSize().getWidth())) {
				Rectangle utRegion = new Rectangle(positionTemplate.getUtBarcodePosition().width,
						positionTemplate.getUtBarcodePosition().height, positionTemplate.getUtBarcodeSize().width,
						positionTemplate.getUtBarcodeSize().height);
				detections = chain.scanRegion(image, utRegion,
						baseOptions.toBuilder().preferredRegion(utRegion).build());
			} else {
				log.debug("Skipping Template. ImageWidth={}; TemplateWidth={}", image.getWidth(),
						positionTemplate.getImageSize() != null
								? Math.round(positionTemplate.getImageSize().getWidth())
								: -1);
				detections = List.of();
			}

			Optional<UnitTrayMatch> match = selectUnitTrayDetection(detections);
			if (match.isPresent()) {
				rawJson = match.get().text();
				resultLabel = match.get().label();
				barcodeStatus = RESULT_BARCODE_SCANNED;
			}
		}

		unitTrayTaxonLabelTextStatus = barcodeStatus;
		if (resultLabel != null && rawJson != null && barcodeStatus == RESULT_BARCODE_SCANNED) {
			labelText = rawJson;
			unitTrayLabel = resultLabel;
			templateUsed = positionTemplate;
		} else {
			labelText = null;
		}
		return resultLabel;
	}

	/**
	 * Return the text found by OCR of the taxon label ({@code getTextPosition})
	 * region of the image according to the specified template.
	 *
	 * @param aTemplate
	 *            template specifying the OCR text region
	 * @return OCR text string (or {@code null} if image/region unavailable)
	 * @throws OCRReadException
	 *             on fatal OCR failure
	 */
	public String getTaxonLabelOCRText(PositionTemplate aTemplate) throws OCRReadException {
		if (labelText == null) {
			barcodeStatus = RESULT_ERROR;
			BufferedImage image = loadDocumentImage();
			if (image != null && aTemplate != null && aTemplate.getTextPosition() != null
					&& aTemplate.getTextSize() != null) {
				int x = aTemplate.getTextPosition().width;
				int y = aTemplate.getTextPosition().height;
				int w = aTemplate.getTextSize().width;
				int h = aTemplate.getTextSize().height;

				OcrEngine engine = ocrEngine != null ? ocrEngine : new TesseractCliOcrEngine();
				Optional<BufferedImage> cropOpt = ImagePreprocessor.copySubimage(image, new Rectangle(x, y, w, h));
				if (cropOpt.isEmpty()) {
					log.debug("OCR text region ({},{},{}x{}) lies outside image bounds {}x{}", x, y, w, h,
							image.getWidth(), image.getHeight());
					return null;
				}
				OcrResult first = engine.recognize(cropOpt.get(), TesseractCliOcrEngine.DEFAULT_LANGUAGE);
				if (first.isSuccess()) {
					labelText = first.getText();
				} else {
					log.error(first.getErrorMessage());
					labelText = "";
				}

				if (labelText == null || labelText.isEmpty()) {
					Optional<BufferedImage> shiftedOpt = ImagePreprocessor.copySubimage(image,
							new Rectangle(x + 1, y + 1, w + 1, h + 1));
					if (shiftedOpt.isPresent()) {
						OcrResult second = engine.recognize(shiftedOpt.get(), TesseractCliOcrEngine.DEFAULT_LANGUAGE);
						if (second.isSuccess()) {
							labelText = second.getText();
						} else {
							log.error(second.getErrorMessage());
							labelText = "";
						}
					}
				}
			}
		}
		return labelText;
	}

	/**
	 * Get the text, if any, from the Exif UserComment or XMP dc:description of the
	 * image file.
	 *
	 * @return the content of the Exif UserComment decoded as a string, or empty
	 *         string if absent
	 */
	public String getExifUserCommentText() {
		if (exifCommentText == null) {
			exifCommentText = imageDocument != null ? imageDocument.getMetadata().effectiveUserComment() : "";
		}
		return exifCommentText;
	}

	/**
	 * Get the metadata creation date from this file formatted according to
	 * {@code format}.
	 *
	 * @param format
	 *            the {@link SimpleDateFormat} pattern of the desired date
	 * @return the formatted date, or {@code null} if none is found
	 */
	public String getExifDateCreated(String format) {
		if (dateCreated == null && imageDocument != null) {
			dateCreated = imageDocument.getMetadata().getDateCreated();
		}
		if (dateCreated != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(dateCreated);
		}
		return null;
	}

	/**
	 * Get the metadata creation date formatted as {@code dd MMMMM yyyy}.
	 *
	 * @return the created date, or {@code null} if none is found
	 */
	public String getExifDateCreated() {
		return getExifDateCreated("dd MMMMM yyyy");
	}

	/**
	 * Get the identifying barcode using the detected {@link #getTemplateUsed()}.
	 *
	 * @return the barcode/identifier
	 */
	public String getBarcodeTextAtFoundTemplate() {
		return getBarcodeText(this.templateUsed);
	}

	/**
	 * Scan part of an image for a catalogNumberBarcode, as specified by the
	 * {@code BarcodePosition} of a {@link PositionTemplate}.
	 *
	 * @param positionTemplate
	 *            the position template specifying where the catalog barcode resides
	 * @return a text string representing the content of the barcode, or an
	 *         error/diagnostic message (check
	 *         {@link #getCatalogNumberBarcodeStatus()})
	 */
	public String getBarcodeText(PositionTemplate positionTemplate) {
		if (catalogNumberBarcodeText != null && !catalogNumberBarcodeText.trim().isEmpty()
				&& catalogNumberBarcodeStatus == RESULT_BARCODE_SCANNED) {
			return catalogNumberBarcodeText;
		}
		if (positionTemplate == null) {
			barcodeStatus = RESULT_ERROR;
			catalogNumberBarcodeStatus = RESULT_ERROR;
			return null;
		}

		String returnValue;
		if (PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(positionTemplate.getTemplateId())) {
			returnValue = getBarcodeText();
		} else {
			barcodeStatus = RESULT_ERROR;
			BufferedImage image = loadDocumentImage();
			if (image == null) {
				returnValue = null;
			} else if (positionTemplate.getBarcodeULPosition() != null && positionTemplate.getBarcodeSize() != null
					&& positionTemplate.getImageSize() != null
					&& image.getWidth() > positionTemplate.getBarcodeULPosition().width
					&& image.getWidth() == Math.round(positionTemplate.getImageSize().getWidth())) {
				Rectangle region = new Rectangle(positionTemplate.getBarcodeULPosition().width,
						positionTemplate.getBarcodeULPosition().height, positionTemplate.getBarcodeSize().width,
						positionTemplate.getBarcodeSize().height);
				ScanOptions options = ScanOptions.fromProperties(readSingletonProperties()).toBuilder()
						.preferredRegion(region).build();
				List<BarcodeDetection> detections = BarcodeScannerChain.fromSingleton().scanRegion(image, region,
						options);
				String selected = selectCatalogDetectionText(detections);
				if (selected != null && !selected.isEmpty()) {
					returnValue = selected;
					barcodeStatus = RESULT_BARCODE_SCANNED;
				} else {
					returnValue = "Failed to read a barcode from templated location.";
					barcodeStatus = RESULT_ERROR;
				}
			} else {
				returnValue = "Image is different size from Template.";
				barcodeStatus = RESULT_ERROR;
			}
		}

		catalogNumberBarcodeStatus = barcodeStatus;
		if (barcodeStatus == RESULT_BARCODE_SCANNED) {
			catalogNumberBarcodeText = returnValue;
		}
		return returnValue;
	}

	/**
	 * Scan the entire image for any QR Code barcode without modifying
	 * {@link #getCatalogNumberBarcodeStatus()}.
	 *
	 * @return a string representing the text of the barcode, or {@code null} if
	 *         none found or unreadable
	 */
	public String getBarcodeText() {
		barcodeStatus = RESULT_ERROR;
		BufferedImage image = loadDocumentImage();
		if (image == null) {
			return null;
		}
		ScanOptions options = ScanOptions.fromProperties(readSingletonProperties());
		List<BarcodeDetection> detections = BarcodeScannerChain.fromSingleton().scanAll(image, options);
		String selected = selectFullImageDetectionText(detections);
		if (selected != null && !selected.isEmpty()) {
			barcodeStatus = RESULT_BARCODE_SCANNED;
			return selected;
		}
		return null;
	}

	/**
	 * Obtain the result of the most recent attempt to check this image for any
	 * barcode.
	 *
	 * @return one of {@link #RESULT_ERROR}, {@link #RESULT_NOT_CHECKED}, or
	 *         {@link #RESULT_BARCODE_SCANNED}
	 */
	public int getBarcodeStatus() {
		return barcodeStatus;
	}

	/**
	 * @return the catalogNumberBarcodeStatus
	 */
	public int getCatalogNumberBarcodeStatus() {
		return catalogNumberBarcodeStatus;
	}

	/**
	 * @return the unitTrayTaxonLabelTextStatus
	 */
	public int getUnitTrayTaxonLabelTextStatus() {
		return unitTrayTaxonLabelTextStatus;
	}

	/**
	 * @return the templateUsed
	 */
	public PositionTemplate getTemplateUsed() {
		return templateUsed;
	}

	/**
	 * @param templateUsed
	 *            the templateUsed to set
	 */
	public void setTemplateUsed(PositionTemplate templateUsed) {
		this.templateUsed = templateUsed;
	}

	private BufferedImage loadDocumentImage() {
		if (imageDocument == null) {
			return null;
		}
		try {
			return imageDocument.getImage();
		} catch (IOException e) {
			log.error("Error decoding image {}: {}", imageDocument.getPath(), e.getMessage());
			return null;
		}
	}

	private Properties readSingletonProperties() {
		try {
			return Singleton.getSingletonInstance().getProperties().getProperties();
		} catch (Exception e) {
			return new Properties();
		}
	}

	private boolean isOcrEnabled() {
		String raw = readSingletonProperties().getProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false");
		return Boolean.parseBoolean(raw.trim());
	}

	private BarcodeMatcher readBarcodeMatcher() {
		try {
			return Singleton.getSingletonInstance().getBarcodeMatcher();
		} catch (Exception e) {
			return null;
		}
	}

	private String selectCatalogDetectionText(List<BarcodeDetection> detections) {
		if (detections == null || detections.isEmpty()) {
			return null;
		}
		BarcodeMatcher matcher = readBarcodeMatcher();
		if (matcher != null) {
			for (BarcodeDetection detection : detections) {
				if (matchesPatternSafely(matcher, detection.text())) {
					return detection.text();
				}
			}
		}
		for (BarcodeDetection detection : detections) {
			if (!looksLikeJsonObject(detection.text())) {
				return detection.text();
			}
		}
		return null;
	}

	private String selectFullImageDetectionText(List<BarcodeDetection> detections) {
		String catalogOrPlain = selectCatalogDetectionText(detections);
		if (catalogOrPlain != null) {
			return catalogOrPlain;
		}
		return detections == null || detections.isEmpty() ? null : detections.get(0).text();
	}

	private Optional<UnitTrayMatch> selectUnitTrayDetection(List<BarcodeDetection> detections) {
		if (detections == null || detections.isEmpty()) {
			return Optional.empty();
		}
		for (BarcodeDetection detection : detections) {
			String text = detection.text();
			if (!looksLikeJsonObject(text)) {
				continue;
			}
			try {
				UnitTrayLabel parsed = UnitTrayLabel.createFromJSONString(text);
				if (parsed != null) {
					return Optional.of(new UnitTrayMatch(text, parsed));
				}
			} catch (Exception e) {
				log.debug("Failed parsing UnitTrayLabel JSON: {}", e.getMessage());
			}
		}
		return Optional.empty();
	}

	private boolean matchesPatternSafely(BarcodeMatcher matcher, String text) {
		if (text == null || text.isBlank()) {
			return false;
		}
		try {
			return matcher.matchesPattern(text);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean looksLikeJsonObject(String value) {
		if (value == null) {
			return false;
		}
		String trimmed = value.trim();
		return trimmed.startsWith("{") && trimmed.endsWith("}");
	}

	private record UnitTrayMatch(String text, UnitTrayLabel label) {
	}
}
