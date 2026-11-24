/**
 * BulkMediaExporter.java
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

import edu.harvard.mcz.imagecapture.data.BulkMedia;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.scanning.BarcodeDetection;
import edu.harvard.mcz.imagecapture.scanning.BarcodeScannerChain;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.ImageMetadata;
import edu.harvard.mcz.imagecapture.scanning.ScanOptions;
import edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CLI and batch export helper for extracting {@link BulkMedia} records and CSV
 * barcode rows from specimen image files.
 */
public final class BulkMediaExporter {

	private static final Logger log = LoggerFactory.getLogger(BulkMediaExporter.class);

	private BulkMediaExporter() {
	}

	/**
	 * Self-standing CLI entry point to extract barcodes from image files or launch
	 * the {@link BulkMediaFrame} GUI.
	 *
	 * @param args
	 *            command line arguments, run with {@code -h} for help
	 */
	public static void main(String[] args) {
		ImageCaptureProperties properties = new ImageCaptureProperties();
		Singleton.getSingletonInstance().setProperties(properties);
		log.debug("Properties loaded");

		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		options.addOption("f", "file", true, "Check one file for Barcodes.");
		options.addOption("h", "help", false, "Get help.");
		options.addOption("u", "ui", false, "Launch UI to check a directory.");
		try {
			CommandLine cmd = parser.parse(options, args);
			boolean hasFile = cmd.hasOption("file");
			boolean hasHelp = cmd.hasOption("help");
			if (hasFile) {
				int exit = 1;
				String filename = cmd.getOptionValue("file");
				String line = parseOneFile(filename);
				if (line != null) {
					System.out.println(line);
					exit = 0;
				}
				System.exit(exit);
			} else if (hasHelp) {
				throw new ParseException("No option specified.");
			} else {
				try {
					showBulkMediaGUI();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("BulkMediaExporter", "Check files for barcodes.", options,
					"Specify filename to check.  \nDefault if no options are selected is to launch a GUI.", true);
			System.exit(1);
		}
	}

	/**
	 * Launches the {@link BulkMediaFrame} Swing GUI on the Event Dispatch Thread.
	 */
	public static void showBulkMediaGUI() {
		javax.swing.SwingUtilities.invokeLater(() -> {
			BulkMediaFrame frame = new BulkMediaFrame();
			frame.pack();
			frame.setVisible(true);
		});
	}

	/**
	 * Parses a single image file into a quoted CSV row containing filename and
	 * barcode/comment text.
	 *
	 * @param filename
	 *            path to the image file
	 * @return CSV line {@code "filename","barcode"}, or {@code null} if unreadable
	 */
	public static String parseOneFile(String filename) {
		if (filename == null || filename.isBlank()) {
			return null;
		}
		File f = new File(filename);
		ImageDocument document = ImageDocument.of(Paths.get(filename));
		try {
			document.ensureReadable();
			ImageMetadata metadata = document.getMetadata();
			logImageDimensionCheck(document, metadata);

			String exif = metadata.effectiveUserComment();
			String scan = scanWholeImageBarcode(document);
			if (scan != null && scan.startsWith("{\"m1p\":")) {
				return '"' + f.getName() + "\",\"" + exif + '"';
			}
			if (scan != null && scan.equals(exif)) {
				return '"' + f.getName() + "\",\"" + scan + '"';
			}
			return '"' + f.getName() + "\",\"" + exif + '"';
		} catch (UnreadableFileException e) {
			log.error(e.getMessage());
			System.out.println("Unable To Read  " + filename);
			return null;
		}
	}

	/**
	 * Produces a {@link BulkMedia} record suitable for loading into the MCZbase
	 * media bulkloader from an image file.
	 *
	 * @param filename
	 *            the filename to load as bulk media
	 * @return {@link BulkMedia} object for that file
	 */
	public static BulkMedia parseOneFileToBulkMedia(String filename) {
		BulkMedia result = new BulkMedia();
		if (filename == null || filename.isBlank()) {
			return result;
		}
		File f = new File(filename);
		ImageDocument document = ImageDocument.of(f);
		try {
			document.ensureReadable();
			ImageMetadata metadata = document.getMetadata();
			String exif = metadata.effectiveUserComment();
			String scan = scanWholeImageBarcode(document);
			String madeDate = metadata.formatDateCreated("dd MMMMM yyyy");
			log.debug("EXIF made date: {}", madeDate);
			if (madeDate != null) {
				result.setMadeDate(madeDate);
			}
			result.setOriginalFilename(f.getName());
			String barcode;
			if (scan != null && scan.startsWith("{\"m1p\":")) {
				barcode = exif;
			} else if (scan != null && scan.equals(exif)) {
				barcode = scan;
			} else {
				barcode = exif;
			}

			BarcodeMatcher matcher = Singleton.getSingletonInstance().getBarcodeMatcher();
			BarcodeBuilder builder = Singleton.getSingletonInstance().getBarcodeBuilder();
			if (matcher != null && builder != null && matcher.matchesPattern(barcode)) {
				result.setCatalogNumber(builder.makeGuidFromBarcode(barcode));
			}
			if (filename.startsWith("http://")) {
				result.setMedia_URI(filename);
			} else {
				File previewFile = ThumbnailBuilder.getThumbFileForFile(f);
				if (!result.setURI(f)) {
					System.out.println("Can't extract URI from path for " + filename);
				}
				if (!result.setPreviewURI(previewFile)) {
					System.out.println("Can't extract URI from path for preview file " + previewFile.getName());
				}
			}
			log.debug("Made date: {}", madeDate);
		} catch (UnreadableFileException e) {
			log.error(e.getMessage());
			System.out.println("Unable To Read  " + filename);
		}
		log.debug("Parsed bulk media result: {}", result);
		return result;
	}

	private static String scanWholeImageBarcode(ImageDocument document) {
		BufferedImage image;
		try {
			image = document.getImage();
		} catch (IOException e) {
			log.error("Error decoding image {}: {}", document.getPath(), e.getMessage());
			return null;
		}
		if (image == null) {
			return null;
		}
		ScanOptions options = ScanOptions
				.fromProperties(Singleton.getSingletonInstance().getProperties().getProperties());
		List<BarcodeDetection> detections = BarcodeScannerChain.fromSingleton().scanAll(image, options);
		if (detections.isEmpty()) {
			return null;
		}
		BarcodeMatcher matcher = Singleton.getSingletonInstance().getBarcodeMatcher();
		if (matcher != null) {
			for (BarcodeDetection detection : detections) {
				if (matcher.matchesPattern(detection.text())) {
					return detection.text();
				}
			}
		}
		return detections.get(0).text();
	}

	/**
	 * Checks image height, width, and EXIF orientation metadata, logging
	 * diagnostics at debug level. Does nothing when debug logging is disabled.
	 *
	 * @param aFile
	 *            image file to check
	 */
	public static void debugCheckHeightWidth(File aFile) {
		if (!log.isDebugEnabled() || aFile == null) {
			return;
		}
		ImageDocument document = ImageDocument.of(aFile);
		ImageMetadata metadata = document.getMetadata();
		logImageDimensionCheck(document, metadata);
	}

	private static void logImageDimensionCheck(ImageDocument document, ImageMetadata metadata) {
		if (!log.isDebugEnabled()) {
			return;
		}
		try {
			BufferedImage image = document.getImage();
			if (image == null) {
				return;
			}
			int iHeight = image.getHeight();
			int iWidth = image.getWidth();
			log.debug("Image Height:{}", iHeight);
			log.debug("Image Width:{}", iWidth);
			int orientation = metadata.getOrientation();
			int width = metadata.getWidth();
			int height = metadata.getHeight();
			log.debug("Orientation: {}", orientation);
			log.debug("Exif Height: {}", height);
			log.debug("Exif Width: {}", width);
			if (width > 0 && height > 0 && (height != iHeight || width != iWidth)) {
				log.error(
						"Warning: Image orientation height/width does not match image height/width.  Image will not display as expected.");
			}
			if (orientation > 1) {
				if (orientation == 6 || orientation == 8) {
					if (height > width) {
						log.error("Warning: Image exif specifies a transformed orientation: " + orientation
								+ ". Image will not display as expected. ");
					} else {
						log.debug("Image exif specifies a transformed orientation: " + orientation
								+ ", which matches aspect ratio. Image may or may not display as expected. ");
					}
				} else {
					log.error("Warning: Image exif specifies a transformed orientation: " + orientation
							+ ". Image will not display as expected. ");
				}
			}
		} catch (Exception e) {
			log.error("Error checking orientation. {}", e.getMessage());
		}
	}
}
