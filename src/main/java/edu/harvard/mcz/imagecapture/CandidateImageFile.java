/**
 * java
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

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.properties.XMPProperty;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.xmp.XmpDirectory;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import edu.harvard.mcz.imagecapture.data.BulkMedia;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame;
import edu.harvard.mcz.imagecapture.utility.GZipCompressor;
import georegression.struct.shapes.Polygon2D_F64;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
// Additional imports for Python fallback
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Image File that might contain text that can be extracted by OCR or a barcode
 * that can be extracted by barcode recognition. <p>
 * TODO: extract few methods to some DataExtractor class or something.
 * This class clearly does more than what's expected of a ("Candidate", Image)
 * File
 */
public class CandidateImageFile {

    public static final int RESULT_NOT_CHECKED = 0;
    public static final int RESULT_BARCODE_SCANNED = 1;
    public static final int RESULT_ERROR = 2;

    // initial pixel width to try rescaling barcode areas to
    public static final double INITIAL_SCALING_WIDTH = 800d;

    /**
     * Matrix used for sharpening images for barcode detection.
     */
    private static final float[] matrix = {0f, -.5f, 0f, -.5f, 2.8f,
            -.5f, 0f, -.5f, 0f};
    private static final Logger log =
            LoggerFactory.getLogger(CandidateImageFile.class);
    private File candidateFile;
    /**
     * Status of last attempted barcode read.
     */
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
     * file.
     *
     * @param aFile
     * @throws UnreadableFileException
     */
    public CandidateImageFile(File aFile) throws UnreadableFileException {
        if (!aFile.canRead()) {
            try {
                throw new UnreadableFileException("Can't read file " +
                        aFile.getCanonicalPath());
            } catch (IOException e) {
                throw new UnreadableFileException(
                        "IOException on trying to get filename.");
            }
        }
        PositionTemplate template = new PositionTemplate();

        init();
        candidateFile = aFile;

        // detect template to use.
        ConfiguredBarcodePositionTemplateDetector detector =
                new ConfiguredBarcodePositionTemplateDetector();

        // First try a quick check of all the templates
        String templateName = detector.detectTemplateForImage(aFile, this, true);
        if (templateName.equals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            // If that fails, try each template more exhaustively.
            templateName = detector.detectTemplateForImage(aFile, this, false);
        }

        try {
            template = new PositionTemplate(templateName);
            log.info("template ID is " + template.getTemplateId());
        } catch (NoSuchTemplateException e) {
            log.error(
                    "Position template detector returned an unknown template name: " +
                            templateName + ".",
                    e);
        }

        // check the file for an exif comment
        getExifUserCommentText(); // scan for exif when handed file.
        if (!template.getTemplateId().equals(
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            try {
                if (getTaxonLabelQRText(template) == null) {
                    getTaxonLabelOCRText(template);
                }
            } catch (OCRReadException e) {
                log.error("Unable to OCR file: " + candidateFile.getName() + " " +
                        e.getMessage());
            }
        }
        templateUsed = template;
    }

    public String getLabelText() {
        return this.labelText;
    }

    /**
     * Constructor
     *
     * @param aFile     the image file that may contain a barcode or text.
     * @param aTemplate the PositionTemplate to use to identify where a barcode or
     *                  OCR text may occur
     *                  in the image provided by aFile.
     * @throws UnreadableFileException if aFile cannot be read.
     * @throws OCRReadException
     */
    public CandidateImageFile(File aFile, PositionTemplate aTemplate)
            throws UnreadableFileException {
        setFile(aFile, aTemplate);
        if (!aFile.canRead()) {
            try {
                throw new UnreadableFileException("Can't read file " +
                        aFile.getCanonicalPath());
            } catch (IOException e) {
                throw new UnreadableFileException(
                        "IOException on trying to get filename.");
            }
        }
    }

    /**
     * Constructor with no parameters to use to access convenience static methods.
     * Must follow with setFile() to use for processing images.
     *
     * @see this.setFile(...);
     */
    public CandidateImageFile() {
    }

    /**
     * Self standing starting point to extract barcodes from image files.
     *
     * @param args command line arguments, run with none or -h for help.
     */
    public static void main(String[] args) {
        // Load properties
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
                // by default, run ui.
                try {
                    showBulkMediaGUI();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(
                    "CandidateImageFile", "Check files for a barcodes.", options,
                    "Specify filename to check.  \nDefault if no options are selected is to launch a GUI.",
                    true);
            System.exit(1);
        }
    }

    /**
     * Checks image height, width and exif metadata height width and orientation,
     * writes to debug log. Does nothing if logging level does not include debug.
     *
     * @param aFile to check
     */
    public static void debugCheckHeightWidth(File aFile) {

        if (log.isDebugEnabled()) {
            // Only take action if log level includes debug.
            try {
                int iHeight = 0;
                int iWidth = 0;
                try {
                    BufferedImage image = ImageIO.read(aFile);
                    iHeight = image.getHeight();
                    iWidth = image.getWidth();
                    log.debug("Image Height:" + iHeight);
                    log.debug("Image Width:" + iWidth);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }

                try {
                    Metadata metadata = ImageMetadataReader.readMetadata(aFile);
                    Directory directory =
                            metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                    JpegDirectory jpegDirectory =
                            metadata.getFirstDirectoryOfType(JpegDirectory.class);

                    try {
                        int orientation =
                                directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                        int width = jpegDirectory.getImageWidth();
                        int height = jpegDirectory.getImageHeight();
                        log.debug("Orientation: " + orientation);
                        log.debug("Exif Height: " + height);
                        log.debug("Exif Width: " + width);
                        if (height != iHeight || width != iWidth) {
                            log.error(
                                    "Warning: Image orientation height/width does not match image height/width.  Image will not display as expected.");
                        }
                        if (orientation > 1) {
                            if (orientation == 6 || orientation == 8) {
                                if (height > width) {
                                    log.error(
                                            "Warning: Image exif specifies a transformed orientation: " +
                                                    orientation + ". Image will not display as expected. ");
                                } else {
                                    log.debug(
                                            "Image exif specifies a transformed orientation: " +
                                                    orientation +
                                                    ", which matches aspect ratio. Image may or may not display as expected. ");
                                }
                            } else {
                                log.error(
                                        "Warning: Image exif specifies a transformed orientation: " +
                                                orientation + ". Image will not display as expected. ");
                            }
                        }
                    } catch (MetadataException e) {
                        log.debug("Error reading EXIF orientation metadata." +
                                e.getMessage());
                    }
                } catch (NullPointerException | ImageProcessingException e1) {
                    log.debug("Error processing EXIF data." + e1.getMessage());
                } catch (IOException e1) {
                    log.error("Error reading file. " + e1.getMessage());
                }
            } catch (Exception eCatchAll) {
                // Eat any exception raised to make sure this debugging routine doesn't
                // stop a working production process.
                log.error("Error checking orientiation. " + eCatchAll.getMessage());
            }
        }
    }

    protected static void showBulkMediaGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BulkMediaFrame frame = new BulkMediaFrame();
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    protected static String parseOneFile(String filename) {
        String result = null;
        File f = new File(filename);
        debugCheckHeightWidth(f);
        try {
            CandidateImageFile file = new CandidateImageFile(
                    f,
                    new PositionTemplate(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS));
            String exif = file.getExifUserCommentText();
            String scan = file.getBarcodeText();
            if (scan.startsWith("{\"m1p\":")) {
                result = '"' + f.getName() + "\",\"" + exif + '"';
            } else {
                if (scan.equals(exif)) {
                    result = '"' + f.getName() + "\",\"" + scan + '"';
                } else {
                    result = '"' + f.getName() + "\",\"" + exif + '"';
                }
            }
        } catch (UnreadableFileException e) {
            log.error(e.getMessage());
            System.out.println("Unable To Read  " + filename);
        } catch (NoSuchTemplateException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Produce a BulkMedia record suitable for loading into the MCZbase bulk media
     * bulkloader from an image file.
     *
     * @param filename the filename to load as bulk media.
     * @return BulkMedia objeect for that file.
     */
    public static BulkMedia parseOneFileToBulkMedia(String filename) {
        BulkMedia result = new BulkMedia();
        File f = new File(filename);
        try {
            CandidateImageFile file = new CandidateImageFile(
                    f,
                    new PositionTemplate(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS));
            String exif = file.getExifUserCommentText();
            String scan = file.getBarcodeText();
            String madeDate = file.getExifDateCreated();
            log.debug("Debug {}", madeDate);
            if (madeDate != null) {
                result.setMadeDate(madeDate);
            }
            String barcode = null;
            result.setOriginalFilename(f.getName());
            if (scan.startsWith("{\"m1p\":")) {
                barcode = exif;
            } else {
                if (scan.equals(exif)) {
                    barcode = scan;
                } else {
                    barcode = exif;
                }
            }
            if (Singleton.getSingletonInstance().getBarcodeMatcher().matchesPattern(
                    barcode)) {
                result.setCatalogNumber(Singleton.getSingletonInstance()
                        .getBarcodeBuilder()
                        .makeGuidFromBarcode(barcode));
            }
            if (filename.startsWith("http://")) {
                result.setMedia_URI(filename);
            } else {
                File preview_file = ThumbnailBuilder.getThumbFileForFile(f);
                // TODO: Add preview uri.
                if (!result.setURI(f)) {
                    System.out.println("Can't extract URI from path for " + filename);
                }
                if (!result.setPreviewURI(preview_file)) {
                    System.out.println("Can't extract URI from path for preview file " +
                            preview_file.getName());
                }
            }
            log.debug("Debug {}", madeDate);
        } catch (UnreadableFileException e) {
            log.error(e.getMessage());
            System.out.println("Unable To Read  " + filename);
        } catch (NoSuchTemplateException e) {
            log.error(e.getMessage());
        }
        log.debug("Debug {}", result);
        return result;
    }

    private static String getQRCodeText(BinaryBitmap bitmap)
            throws NotFoundException, ChecksumException, FormatException {
        Result result;
        String returnValue;
        QRCodeReader reader = new QRCodeReader();
        Hashtable<DecodeHintType, Object> hints = null;
        hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        result = reader.decode(bitmap, hints);
        returnValue = result.getText();
        return returnValue;
    }

    /**
     * Convenience method to check an image for a barcode.  Does not set any
     * instance variables of CandidateImageFile, and does not behave precisely as
     * the getBarcodeText() methods.  Result state is not available from
     * getBarcodeStatus() and both errors and the absence of a barcode in the
     * image result in an empty string being returned. If a template is specified
     * and no barcode is detected, tries again with some image scaling and
     * contrast variations.
     *
     * @param image            The BufferedImage to check for a barcode.
     * @param positionTemplate The position template specifying where in the image
     *                         to check for the barcode, if
     *                         TEMPLATE_NO_COMPONENT_PARTS, the entire image is
     *                         checked for a barcode, otherwise only the part of the image specified by
     *                         the template is checked.
     * @return the text of the barcode found in the barcode portion of the
     * position template, or an empty string.
     */
    public String getBarcodeTextFromImage(BufferedImage image,
                                          PositionTemplate positionTemplate,
                                          boolean quickCheck) {

        log.debug("Debug {}", positionTemplate.getName());
        String returnValue = "";
        if (positionTemplate.getTemplateId().equals(
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            // Check the entire image for a barcode and return.
            log.debug("Debug {}", image.getType());
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            CandidateImageFile temp = new CandidateImageFile();
            TextStatus checkResult = temp.checkSourceForBarcode(source, true);
            returnValue = checkResult.getText();
        } else {
            // Check the part of the image specified by the template for the barcode.
            if (image != null) {
                if (image.getWidth() > positionTemplate.getBarcodeULPosition().width) {
                    // image might plausibly match template
                    int left = positionTemplate.getBarcodeULPosition()
                            .width; //* @param left x coordinate of leftmost pixels
                    // to decode
                    int top = positionTemplate.getBarcodeULPosition()
                            .height; //* @param top y coordinate of topmost pixels
                    // to decode
                    int right =
                            left +
                                    positionTemplate.getBarcodeSize()
                                            .width; //* @param right one more than the x coordinate of
                    // rightmost pixels to decode. That is, we will decode
                    int width = positionTemplate.getBarcodeSize().width;
                    //*  pixels whose x coordinate is in [left,right)
                    int bottom =
                            top +
                                    positionTemplate.getBarcodeSize()
                                            .height; //* @param bottom likewise, one more than the y
                    // coordinate of the bottommost pixels to decode
                    int height = positionTemplate.getBarcodeSize().height;

                    returnValue = readBarcodeFromLocation(image, left, top, width, height,
                            quickCheck);
                }
            }
        }
        return returnValue;
    }

    /**
     * Convenience method to check an image for a barcode.  Does not set any
     * instance variables of CandidateImageFile, and does not behave precisely as
     * the getBarcodeText() methods.  Result state is not available from
     * getBarcodeStatus() and both errors and the absence of a barcode in the
     * image result in an empty string being returned. <p> Attempts read of
     * relevant crop from image, then attempts this with crop area scaled down,
     * then attempts it with crop area sharpened.  Does not include shifts of
     * location of crop area.
     *
     * @param image            The BufferedImage to check for a barcode.
     * @param positionTemplate The position template specifying where in the image
     *                         to check for the barcode, if
     *                         TEMPLATE_NO_COMPONENT_PARTS, the entire image is
     *                         checked for a barcode, otherwise only the part of the image specified by
     *                         the template for the UnitTrayLabel is checked.
     * @return the text of the barcode found in the UnitTrayLabel (text) portion
     * of the position template, or an empty string.
     */
    public String
    getBarcodeUnitTrayTextFromImage(BufferedImage image,
                                    PositionTemplate positionTemplate) {
        String returnValue = "";
        if (positionTemplate.getTemplateId().equals(
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            // Check the entire image for a barcode and return.
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Result result;
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                returnValue = getQRCodeText(bitmap);
            } catch (ReaderException e) {
                returnValue = "";
            }
        } else {
            // Check the part of the image specified by the template for the barcode.
            if (image != null) {
                if (image.getWidth() > positionTemplate.getUtBarcodePosition().width) {
                    // image might plausibly match template
                    int left = positionTemplate.getUtBarcodePosition()
                            .width; //* @param left x coordinate of leftmost pixels
                    // to decode
                    int top = positionTemplate.getUtBarcodePosition()
                            .height; //* @param top y coordinate of topmost pixels
                    // to decode
                    int right =
                            left +
                                    positionTemplate.getUtBarcodeSize()
                                            .width; //* @param right one more than the x coordinate of
                    // rightmost pixels to decode. That is, we will decode
                    //*  pixels whose x coordinate is in [left,right)
                    int bottom =
                            top +
                                    positionTemplate.getUtBarcodeSize()
                                            .height; //* @param bottom likewise, one more than the y
                    // coordinate of the bottommost pixels to decode
                    int width = positionTemplate.getUtBarcodeSize().width;
                    int height = positionTemplate.getUtBarcodeSize().height;
                    returnValue =
                            readBarcodeFromLocation(image, left, top, width, height, false);
                }
            }
        }
        return returnValue;
    }

    /**
     * Read the barcode content from a portion of an image.
     *
     * @param image  to crop a portion out of to detect a barcode
     * @param left   x coordinate in image of leftmost pixels to decode
     * @param top    y coordinate in image of topmost pixels to decode,  left and
     *               top are the upper left x,y coordinate
     * @param width  in pixels of the area to decode
     * @param height in pixels of the area to decode
     * @return string content of barcode found, or an empty string
     */
    public String readBarcodeFromLocation(BufferedImage image, int left, int top,
                                          int width, int height,
                                          boolean quickCheck) {
        TextStatus returnValue = new TextStatus("", RESULT_ERROR);
        if (image == null) {
            log.error("Image null");
            return "";
        }
        String fileName = candidateFile != null ? candidateFile.getName() : "";
        if (image.getWidth() >= left + width && image.getHeight() >= top + height) {
            // provided crop area falls within image.
            log.debug("Attempting to detect barcode in TL: " + left + "x" + top +
                    " +" + width + "x" + height);
            LuminanceSource source = null;
            try {
                // Create a crop of the image, test this cropped area
                source =
                        new BufferedImageLuminanceSource(image, left, top, width, height);
            } catch (IllegalArgumentException e) {
                log.error("Provided coordinates fall outside bounds of image; Error: " +
                        e.getMessage());
                return "";
            }

            // 1, try the source crop directly.
            returnValue = this.checkSourceForBarcode(source, log.isDebugEnabled());
            log.debug("result of source barcode check: '" + returnValue.getText() +
                    "' at status: " + returnValue.getStatus());

            if (quickCheck) {
                if (returnValue.getText() == null) {
                    returnValue = checkWithDisplacing(image, left, top, width, height);
                }
                return returnValue.getText();
            }
            // If this is not a quick check, keep trying harder
            if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                log.debug(
                        "Successful preprocess barcode read from location using method #1");
                return returnValue.getText();
            }
            // 2, try rescaling (to a 800 pixel width)
            log.debug("Trying again with scaled image crop: " +
                    INITIAL_SCALING_WIDTH + "px.");
            double scale = INITIAL_SCALING_WIDTH / width;
            returnValue = checkWithScale(image, left, top, width, height, scale);

            if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '" + fileName +
                        "' from location using method #2");
                return returnValue.getText();
            }
            // 3, try another barcode scanner
            returnValue =
                    checkSourceForBarcode(image.getSubimage(left, top, width, height));

            if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '" + fileName +
                        "' from location using method #3");
                return returnValue.getText();
            }

            // 4, try another barcode scanner, but this time "global"
            returnValue = checkSourceForBarcodeAt(
                    image, (int) (top + (height / 2.0)), (int) (left + (width / 2.0)),
                    (int) (Math.max(width, height) +
                            0.1 * Math.min(image.getWidth(), image.getHeight())));

            if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '" + fileName +
                        "' from location using method #4");
                return returnValue.getText();
            }

            // 5, try rescaling to configured scaling widths
            String scaling =
                    Singleton.getSingletonInstance()
                            .getProperties()
                            .getProperties()
                            .getProperty(ImageCaptureProperties.KEY_IMAGERESCALE);
            ArrayList<String> scalingBits = new ArrayList<String>();
            if (scaling.contains(",")) {
                scalingBits.addAll(Arrays.asList(scaling.split(",")));
            } else {
                scalingBits.add(scaling);
            }
            for (String transform : scalingBits) {
                returnValue =
                        checkWithTransform(image, left, top, width, height, transform);
                if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                    log.debug("Successful preprocess barcode read of image '" + fileName +
                            "' from location using method #5");
                    log.info("Success at image '" + fileName +
                            "' with transform = " + transform + ".");
                    return returnValue.getText();
                }
            } // end while loop

            // 6, try again with a sharpened image
            try {
                log.debug("Trying again with image sharpened.");
                Kernel kernel = new Kernel(3, 3, matrix);
                ConvolveOp convolver =
                        new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                BufferedImage sharpened = new BufferedImage(
                        image.getWidth(), image.getHeight(), image.getType());
                sharpened = convolver.filter(image, sharpened);
                source = new BufferedImageLuminanceSource(sharpened, left, top, width,
                        height);
                returnValue = this.checkSourceForBarcode(source, true);
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage(), e);
            }

            if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '" + fileName +
                        "' from location using method #6");
                return returnValue.getText();
            }

            // 7, try again with scale factors & offsets
            BufferedImage crop = image.getSubimage(left, top, width, height);
            float[] scaleFactors = {1.2f, 0.8f, 0.6f, 1.4f, 1.6f};
            int[] offsets = {15, -15, -30, 30, 45};
            for (int i = 0; i < scaleFactors.length; ++i) {
                for (int o = 0; o < offsets.length; ++o) {
                    returnValue =
                            checkWithConfiguration(crop, scaleFactors[i], offsets[o], null);
                    if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                        log.debug("Successful preprocess barcode read of image '" +
                                fileName + "' from location using method #7");
                        log.info("Success at image '" + fileName + "' with offset = " +
                                offsets[o] + " and scaleFactor = " + scaleFactors[i]);
                        return returnValue.getText();
                    }
                }
            }

            // 8, Try again with some small displacements of window
            returnValue = checkWithDisplacing(image, left, top, width, height);

            if (returnValue.getStatus() == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '" + fileName +
                        "' from location using method #8");
                return returnValue.getText();
            }
        }

        return ""; // returnValue.getText();
    }

    /**
     * Check an image for a barcode at a certain displacement
     *
     * @param image          the image to check
     * @param left           the original left to find the barcode at
     * @param top            the original top to find the barcode at
     * @param width          the original with of the image
     * @param height         the original height of the image
     * @param transformToTry the specification of the transformation which should
     *                       be applied to check
     * @return the status of the check
     */
    private TextStatus checkWithTransform(BufferedImage image, int left, int top,
                                          int width, int height,
                                          String transformToTry) {
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp convolver = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        TextStatus checkResult = new TextStatus("", RESULT_NOT_CHECKED);
        LuminanceSource source;
        boolean sharpen = false;
        boolean brighter = false;
        boolean dimmer = false;
        //	assert (!(widthToTry.contains("sharpen") &&
        // widthToTry.contains("brighter") && widthToTry.contains("dimmer")));
        if (transformToTry.contains("sharpen")) {
            sharpen = true;
            transformToTry = transformToTry.replace("sharpen", "");
        } else if (transformToTry.contains("brighter")) {
            brighter = true;
            dimmer = false;
            transformToTry = transformToTry.replace("brighter", "");
        } else if (transformToTry.contains("dimmer")) {
            brighter = false;
            dimmer = true;
            transformToTry = transformToTry.replace("dimmer", "");
        }
        // strip out any remaining non-numeric characters (sharpen'ed')
        transformToTry = transformToTry.replaceAll("[^0-9.]", "");
        log.debug("TransformToTry: " + transformToTry);
        // Try rescaling to a configured scaling width value.
        double scalingWidth = 0d;
        try {
            scalingWidth = Double.parseDouble(transformToTry);
            if (scalingWidth < 1) {
                scalingWidth = INITIAL_SCALING_WIDTH / 2;
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        if ((width != scalingWidth && scalingWidth != INITIAL_SCALING_WIDTH) ||
                (sharpen || brighter || dimmer)) {
            try {
                // if a rescaling different from what's been done in steps 1 and 2 has
                // been configured, rescale and try again. skip any configured
                // transforms that are identical to steps 1 or 2.
                log.debug("Trying again with scaled image crop to: " + scalingWidth +
                        "px.");
                double scale = scalingWidth / width;
                int scaledHeight = (int) (height * scale);
                int scaledWidth = (int) (width * scale);
                BufferedImage cropped = image.getSubimage(left, top, width, height);
                int initialH = cropped.getHeight();
                int initialW = cropped.getWidth();
                // BufferedImage scaled = new BufferedImage(initialW, initialH,
                // cropped.getType());
                BufferedImage scaled =
                        new BufferedImage(scaledWidth, scaledHeight, cropped.getType());
                AffineTransform rescaleTransform = new AffineTransform();
                rescaleTransform.scale(scale, scale);
                AffineTransformOp scaleOp = new AffineTransformOp(
                        rescaleTransform, AffineTransformOp.TYPE_BILINEAR);
                scaled = scaleOp.filter(cropped, scaled);
                if (sharpen) {
                    log.debug("Also sharpening the crop");
                    BufferedImage dest =
                            new BufferedImage(scaledWidth, scaledHeight, cropped.getType());
                    scaled = convolver.filter(scaled, dest);
                }
                if (brighter) {
                    log.debug("Also brightening the crop");
                    RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
                    BufferedImage src = scaled;
                    scaled = rescaleOp.filter(src, scaled);
                }
                if (dimmer) {
                    log.debug("Also dimming the crop");
                    RescaleOp rescaleOp = new RescaleOp(0.80f, -15, null);
                    BufferedImage src = scaled;
                    scaled = rescaleOp.filter(src, scaled);
                }
                source = new BufferedImageLuminanceSource(scaled);
                checkResult = this.checkSourceForBarcode(source, true);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return checkResult;
    }

    /**
     * Check an image source for barcode, trysing
     *
     * @param image
     * @param left
     * @param top
     * @param width
     * @param height
     * @return
     */
    private TextStatus checkWithDisplacing(BufferedImage image, int left, int top,
                                           int width, int height) {
        LuminanceSource source;
        TextStatus checkResult = new TextStatus("", RESULT_ERROR);
        doubleLoop:
        for (int shiftLeft = left - 3; shiftLeft <= left + 3;
             shiftLeft = shiftLeft + 6) {
            for (int shiftTop = top - 3; shiftTop <= top + 3;
                 shiftTop = shiftTop + 6) {
                try {
                    log.debug("Trying displacement of crop: " + shiftLeft + "," +
                            shiftTop);
                    source = new BufferedImageLuminanceSource(image, shiftLeft, shiftTop,
                            width, height);
                    checkResult = this.checkSourceForBarcode(source, true);
                    if (checkResult.getStatus() != RESULT_ERROR) {
                        break doubleLoop;
                    }
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    // shift falls outside image, inBounds = false
                }
            }
        }
        return checkResult;
    }

    private TextStatus checkWithConfiguration(BufferedImage crop,
                                              float scaleFactor, int offset,
                                              RenderingHints hints) {
        RescaleOp rescaleOp = new RescaleOp(scaleFactor, offset, hints);
        rescaleOp.filter(crop, crop);
        BufferedImageLuminanceSource cropSource =
                new BufferedImageLuminanceSource(crop);
        return checkSourceForBarcode(cropSource, log.isDebugEnabled());
    }

    private TextStatus checkWithScale(BufferedImage image, int left, int top,
                                      int width, int height, double scale) {
        String returnValue = null;
        int scaledHeight = (int) (height * scale);
        int scaledWidth = (int) (width * scale);
        BufferedImage cropped = image.getSubimage(left, top, width, height);
        int initialH = cropped.getHeight();
        int initialW = cropped.getWidth();
        BufferedImage scaled =
                new BufferedImage(scaledWidth, scaledHeight, cropped.getType());
        AffineTransform rescaleTransform = new AffineTransform();
        rescaleTransform.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(
                rescaleTransform, AffineTransformOp.TYPE_BILINEAR);
        scaled = scaleOp.filter(cropped, scaled);
        BufferedImageLuminanceSource cropSource =
                new BufferedImageLuminanceSource(scaled);
        CandidateImageFile temp = new CandidateImageFile();
        return temp.checkSourceForBarcode(cropSource, log.isDebugEnabled());
    }

    /**
     * Test to see if the file provided in the constructor or the setFile method
     * is readable.  This method is called from both the CandidateImageFile(File
     * aFile, PositionTemplate aTemplate) constructor and the setFile(File aFile,
     * PositionTemplate aTemplate) method, so it shouldn't be necessary to call it
     * externally.
     *
     * @return true if file is readable, throws UnreadableFileException exception
     * rather than returning false
     * if file can't be read.
     * @throws UnreadableFileException if file is null, or if it doesn't exist or
     *                                 if it can't be read.
     */
    public boolean isFileReadable() throws UnreadableFileException {
        try {
            if (candidateFile == null) {
                throw new UnreadableFileException(
                        "No such file. CandidateImageFile given null for a filename.");
            }
            if (!candidateFile.exists()) {
                throw new UnreadableFileException("No such file as: " +
                        candidateFile.getAbsolutePath());
            }
            if (!candidateFile.canRead()) {
                throw new UnreadableFileException("Can't read file: " +
                        candidateFile.getAbsolutePath());
            }
        } catch (SecurityException e) {
            throw new UnreadableFileException(
                    "Can't read file: " + candidateFile.getAbsolutePath() +
                            " Security problem." + e.getMessage());
        }
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
    }

    /**
     * Change the image file and position template.
     *
     * @param aFile
     * @param aTemplate
     * @throws UnreadableFileException
     * @throws OCRReadException
     */
    public void setFile(File aFile, PositionTemplate aTemplate)
            throws UnreadableFileException {
        candidateFile = aFile;
        isFileReadable();
        // Set initial state
        init();

        // check the file for an exif comment
        getExifUserCommentText(); // scan for exif when handed file.
        if (!aTemplate.getTemplateId().equals(
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            try {
                if (getTaxonLabelQRText(aTemplate) == null) {
                    getTaxonLabelOCRText(aTemplate);
                }
            } catch (OCRReadException e) {
                log.error("Unable to OCR file: " + candidateFile.getName() + " " +
                        e.getMessage());
            }
        }
        templateUsed = aTemplate;
    }

    /**
     * Obtain the File for this candidate image file
     *
     * @return the candidateFile
     */
    public File getFile() {
        return candidateFile;
    }

    /**
     * Check a LuminanceSource for a barcode, handle exceptions, and return
     * an object containing the text read (or an error message) and the
     * corresponding value to use for barcodeStatus.
     *
     * @param source LuminanceSource to check for a barcode.
     * @return a TextStatus object containing the barcodeStatus value and the
     * text found (or the error message).
     */
    private TextStatus checkSourceForBarcode(LuminanceSource source,
                                             boolean generateDebugImage) {
        TextStatus returnValue = new TextStatus("", RESULT_NOT_CHECKED);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        if (generateDebugImage) {
            try {
                int h = bitmap.getBlackMatrix().getHeight();
                int w = bitmap.getBlackMatrix().getWidth();
                BufferedImage temp =
                        new BufferedImage(h, w, BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = temp.getGraphics();
                g.setColor(Color.WHITE);
                g.drawRect(0, 0, w, h);
                g.setColor(Color.BLACK);
                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        try {
                            if (bitmap.getBlackMatrix().get(i, j)) {
                                g.setColor(Color.BLACK);
                                g.drawRect(i, j, 1, 1);
                            } else {
                                g.setColor(Color.WHITE);
                                g.drawRect(i, j, 1, 1);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //
                        }
                    }
                }
                Date d = new Date();
                if (log.isTraceEnabled()) {
                    log.debug("Trace is enabled!!!!");
                    String t = Long.toString(d.getTime());
                    File out = new File("TempBarcodeCrop" + t + ".png");
                    ImageIO.write(temp, "png", out);
                    log.trace("Wrote: " + out.getPath());
                } else {
                    ImageIO.write(temp, "png", new File("TempBarcodeCrop.png"));
                }
            } catch (NotFoundException e1) {
                log.error(e1.getMessage(), e1);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.trace("Generate Debug Image: " + generateDebugImage);
        }

        Result result;
        QRCodeReader reader = new QRCodeReader();
        try {
            Hashtable<DecodeHintType, Object> hints = null;
            hints = new Hashtable<DecodeHintType, Object>(3);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.FALSE);
            // Probable bug in xzing, reader.decode can throw
            // ArrayIndexOutOfBoundsException as well as the expected ReaderException.
            // It looks like there's an assumption hidden in the bitmapMatrix that the
            // height and width are the same.
            result = reader.decode(bitmap, hints);
            returnValue.setText(result.getText());
            returnValue.setStatus(RESULT_BARCODE_SCANNED);
        } catch (ReaderException e) {
            log.error(e.getClass() + " " + e.getMessage());
            if (!Singleton.getSingletonInstance()
                    .getProperties()
                    .getProperties()
                    .getProperty(ImageCaptureProperties.KEY_IMAGEZXINGALSOTRYHARDER)
                    .equalsIgnoreCase("true")) {
                returnValue.setText(e + " " + e.getMessage());
                returnValue.setStatus(RESULT_ERROR);
            } else {
                try {
                    Hashtable<DecodeHintType, Object> hints = null;
                    hints = new Hashtable<DecodeHintType, Object>(3);
                    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                    // Probable bug in xzing, reader.decode can throw
                    // ArrayIndexOutOfBoundsException as well as the expected
                    // ReaderException.  It looks like there's an assumption hidden in the
                    // bitmapMatrix that the height and width are the same.
                    result = reader.decode(bitmap, hints);
                    returnValue.setText(result.getText());
                    returnValue.setStatus(RESULT_BARCODE_SCANNED);
                } catch (ReaderException e1) {
                    log.debug(e1 + " " + e1.getMessage());
                    returnValue.setText(e + " " + e1.getMessage());
                    returnValue.setStatus(RESULT_ERROR);
                } catch (ArrayIndexOutOfBoundsException e1) {
                    log.error(e.getMessage());
                    returnValue.setText(e1 + " " + e1.getMessage());
                    returnValue.setStatus(RESULT_ERROR);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error(e.getMessage());
            returnValue.setText(e + " " + e.getMessage());
            returnValue.setStatus(RESULT_ERROR);
        }
        return returnValue;
    }

    /**
     * Check an image source for a barcode using BoofCV scanner
     *
     * @param source the image to check
     * @return the status tuple
     */
    private TextStatus checkSourceForBarcode(BufferedImage source) {
        GrayU8 gray = ConvertBufferedImage.convertFrom(source, new GrayU8(1, 1));

        QrCodeDetector<GrayU8> detector =
                FactoryFiducial.qrcode(null, GrayU8.class);

        detector.process(gray);

        // Get's a list of all the qr codes it could successfully detect and decode
        List<QrCode> detections = detector.getDetections();
        log.debug("BoofCV QRScanner found " + detections.size() +
                " barcodes. Success only if 1. Additionally, failures: " +
                detector.getFailures().size());

        if (detections.size() == 1) {
            for (QrCode detection : detections) {
                return new TextStatus(detection.message, RESULT_BARCODE_SCANNED);
            }
        }
        return new TextStatus("", RESULT_ERROR);
    }

    /**
     * Check an image source for a barcode using BoofCV scanner
     *
     * @param source   the image to check
     * @param fromLeft the expected x distance
     * @param fromTop  the expected y distance
     * @return the status tuple
     */
    private TextStatus checkSourceForBarcodeAt(BufferedImage source, int fromLeft,
                                               int fromTop, int tol) {
        GrayU8 gray = ConvertBufferedImage.convertFrom(source, new GrayU8(1, 1));

        QrCodeDetector<GrayU8> detector =
                FactoryFiducial.qrcode(null, GrayU8.class);

        detector.process(gray);

        // Get's a list of all the qr codes it could successfully detect and decode
        List<QrCode> detections = detector.getDetections();
        log.debug("BoofCV QRScanner found " + detections.size() +
                " barcodes. Will search for nearest. Additionally, failures: " +
                detector.getFailures().size());
        Rectangle toleranceBounds = new Rectangle(fromTop, fromLeft, tol, tol);

        for (QrCode detection : detections) {
            //
            if (this.intersect(detection.bounds, toleranceBounds)) {
                return new TextStatus(decompressMessageIfNecessary(detection),
                        RESULT_BARCODE_SCANNED);
            }
        }

        return new TextStatus("", RESULT_ERROR);
    }

    /**
     * Decompress using GZipCompressor Utility in case a QrCode contains
     * compressed data
     *
     * @param detection
     * @return
     */
    public static String decompressMessageIfNecessary(QrCode detection) {
        String result = "";
        byte[] dataBytes = detection.corrected;
        if (GZipCompressor.isCompressed(dataBytes)) {
            try {
                // try decompress
                result = GZipCompressor.decompress(dataBytes);
            } catch (IOException e) {
                log.error("GZipDecompression failed: " + e.getMessage());
            }
        } else {
            result = detection.message;
        }
        return result;
    }

    private boolean intersect(Polygon2D_F64 first, Rectangle second) {
        assert first.vertexes.size == 4;

        log.debug("BoofCV found QR with top-left: x0 = " + first.get(0).x +
                ", y0 = " + first.get(0).y);
        Rectangle first_p =
                new Rectangle((int) first.get(0).x, (int) first.get(0).y,
                        (int) Math.abs(first.get(2).x - first.get(0).x),
                        (int) Math.abs(first.get(2).y - first.get(0).y));
        log.debug("Translating to rectangle: x0 = " + first_p.x +
                ", y0 = " + first_p.y + ", x3 = " + (first_p.x + first_p.height) +
                ", y3 = " + (first_p.y + first_p.width));
        log.debug("Using tolerance bounds: x0 = " + second.x +
                ", y0 = " + second.y + ", x3 = " + (second.x + second.height) +
                ", y3 = " + (second.y + second.width));
        log.debug("Intersection: " + first_p.intersects(second));
        return first_p.intersects(second);
    }

    /**
     * If the image contains a taxon label text encoded in a QRCode barcode in the
     * position specified for the Taxon/UnitTrayLabel Barcode by the
     * PositionTemplate, return that text as a UnitTrayLabel object.
     *
     * @param positionTemplate, the template specifying the location of the
     *                          barcode via getUtBarcodePosition.
     * @return null or a UnitTrayLabel containing the parsed text of the taxon
     * label read from the barcode.
     */
    public UnitTrayLabel getTaxonLabelQRText(PositionTemplate positionTemplate) {
        log.info("2 template ID is.........." + positionTemplate.getTemplateId());
        log.debug("Debug {}", unitTrayLabel);
        log.debug("Debug {}", unitTrayTaxonLabelTextStatus);
        log.debug("Debug {}", labelText);
        if (unitTrayLabel != null &&
                unitTrayTaxonLabelTextStatus == RESULT_BARCODE_SCANNED &&
                labelText != null && labelText.length() > 0) {
            return unitTrayLabel;
        }

        UnitTrayLabel resultLabel = null;
        String returnValue = "";
        if (positionTemplate.getTemplateId().equals(
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            // Check the entire image for a barcode and return.
            returnValue = getBarcodeText();
        } else {
            // Check the part of the image specified by the template for the barcode.
            BufferedImage image = null;
            String error = "";
            barcodeStatus = RESULT_ERROR;
            try {
                image = ImageIO.read(candidateFile);
                log.debug("Debug {}", candidateFile.getCanonicalFile());
            } catch (IOException e) {
                error = e + " " + e.getMessage();
                returnValue = error;
            }
            if (image == null) {
                returnValue = "Could not decode image. " + error;
                barcodeStatus = RESULT_ERROR;
            } else {
                // Could read image.  Try reading barcode from templated location.
                if (image.getWidth() >= positionTemplate.getUtBarcodePosition().width &&
                        image.getWidth() ==
                                Math.round(positionTemplate.getImageSize().getWidth())) {
                    // image might plausibly match template
                    int left = positionTemplate.getUtBarcodePosition()
                            .width; //* @param left x coordinate of leftmost pixels
                    // to decode
                    int top = positionTemplate.getUtBarcodePosition()
                            .height; //* @param top y coordinate of topmost pixels
                    // to decode
                    int right =
                            left +
                                    positionTemplate.getUtBarcodeSize()
                                            .width; //* @param right one more than the x coordinate of
                    // rightmost pixels to decode. That is, we will decode
                    int width = positionTemplate.getUtBarcodeSize().width;
                    //*  pixels whose x coordinate is in [left,right)
                    int bottom =
                            top +
                                    positionTemplate.getUtBarcodeSize()
                                            .height; //* @param bottom likewise, one more than the y
                    // coordinate of the bottommost pixels to decode
                    int height = positionTemplate.getUtBarcodeSize().height;

                    returnValue =
                            readBarcodeFromLocation(image, left, top, width, height, false);
                    if (returnValue.length() > 0) {
                        barcodeStatus = RESULT_BARCODE_SCANNED;
                    } else {
                        returnValue = "Failed to read a barcode from templated location.";
                        barcodeStatus = RESULT_ERROR;
                    }
                    log.debug("Debug {}", returnValue);
                    log.debug("barcodeStatus=" + barcodeStatus);

                } else {
                    // image is narrower than templated area.
                    log.debug("Skipping Template.  ImageWidth=" + image.getWidth() +
                            "; TemplateWidth=" +
                            Math.round(positionTemplate.getImageSize().getWidth()));
                }

            } // image is readable
        }

        unitTrayTaxonLabelTextStatus = barcodeStatus;

        if (!returnValue.equals("") && barcodeStatus == RESULT_BARCODE_SCANNED) {
            log.debug("Found QR Barcode on unit tray label containing: " +
                    returnValue);
            resultLabel = UnitTrayLabel.createFromJSONString(returnValue);
            labelText = returnValue;
            unitTrayLabel = resultLabel;
            templateUsed = positionTemplate;
        } else {
            labelText = null;
        }
        return resultLabel;
    }

    /**
     * Return the text found by OCR of the taxon label (getTextPosition) region of
     * the image according to the specified template.
     *
     * @param aTemplate
     * @return a string
     * @throws OCRReadException
     */
    public String getTaxonLabelOCRText(PositionTemplate aTemplate)
            throws OCRReadException {
        // Actual read attempt is only invoked once,
        // subsequent calls return cached value.
        log.debug("in getLabelOCRText() 1 labelText is " + labelText);
        if (labelText == null) {
            BufferedImage image = null;
            String error = "";
            barcodeStatus = RESULT_ERROR;
            try {
                image = ImageIO.read(candidateFile);
                log.debug("image is " + image);
            } catch (IOException e) {
                error = e + " " + e.getMessage();
                log.error("Error", error);
            }
            if (image != null) {
                try {
                    if (aTemplate != null &&
                            aTemplate.getTextPosition() != null) { // allie edit
                        int x = aTemplate.getTextPosition().width;
                        int y = aTemplate.getTextPosition().height;
                        int w = aTemplate.getTextSize().width;
                        int h = aTemplate.getTextSize().height;

                        // OCR and parse UnitTray Label
                        ConvertTesseractOCR o =
                                new ConvertTesseractOCR(image.getSubimage(x, y, w, h));
                        labelText = "";
                        try {
                            labelText = o.getOCRText();
                        } catch (OCRReadException e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    log.error("Exception thrown in OCR of unit tray label.");
                    log.error("Error", ex);
                    log.trace("Trace", ex);
                    throw new OCRReadException(ex.getMessage());
                }
                if ((labelText == null || labelText.equals("")) && aTemplate != null &&
                        aTemplate.getTextPosition() !=
                                null) { // this line throws an exception?!
                    try {
                        // try again
                        int x = aTemplate.getTextPosition().width + 1;
                        int y = aTemplate.getTextPosition().height + 1;
                        int w = aTemplate.getTextSize().width + 1;
                        int h = aTemplate.getTextSize().height + 1;

                        log.debug("in getLabelOCRText() 10");
                        // OCR and parse UnitTray Label
                        ConvertTesseractOCR o =
                                new ConvertTesseractOCR(image.getSubimage(x, y, w, h));
                        labelText = "";
                        try {
                            labelText = o.getOCRText();
                        } catch (OCRReadException e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    } catch (Exception ex) {
                        log.error("Exception thrown in OCR of unit tray label.");
                        log.error("Error", ex);
                        log.trace("Trace", ex);
                        throw new OCRReadException(ex.getMessage());
                    }
                }
            }
        }
        return labelText;
    }

    /**
     * Get the text, if any, from the Exif UserComment of the image file.
     *
     * @return the content of the Exif UserComment decoded as a string.
     */
    public String getExifUserCommentText() {
        // Actual read attempt is only invoked once,
        // subsequent calls return cached value.
        if (exifCommentText == null) {
            // read, or re-read the file for a comment
            String exifComment = "";
            try {
                Metadata metadata = JpegMetadataReader.readMetadata(
                        candidateFile, JpegMetadataReader.ALL_READERS);
                // [Exif] User Comment
                Directory exifDirectory =
                        metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                ExifSubIFDDescriptor descriptor =
                        new ExifSubIFDDescriptor((ExifSubIFDDirectory) exifDirectory);
                exifComment = descriptor.getUserCommentDescription();
                log.debug("Exif UserComment = " + exifComment);
            } catch (JpegProcessingException e2) {
                log.error("Error reading exif metadata.");
                log.error(e2.getMessage());
            } catch (NullPointerException e1) {
                log.error(
                        "Error reading exif metadata, ExifSubIFDDirectory not found.");
                log.error(e1.getMessage());
            } catch (IOException e) {
                log.error("Error reading file for exif metadata.");
                log.error(e.getMessage());
            }
            if (exifComment == null || exifComment.trim().length() == 0) {
                // Try to see if there is an xmp dc:description block
                Metadata metadata;
                try {
                    metadata = JpegMetadataReader.readMetadata(
                            candidateFile, JpegMetadataReader.ALL_READERS);

                    XmpDirectory xmpDirectory =
                            metadata.getFirstDirectoryOfType(XmpDirectory.class);
                    if (xmpDirectory != null && xmpDirectory.getXMPMeta() != null) {
                        log.debug(xmpDirectory.getXMPMeta().dumpObject());
            /*
        http://purl.org/dc/elements/1.1/ = "dc:"	(0x80000000 :
        SCHEMA_NODE) dc:description	(0x1e00 : ARRAY | ARRAY_ORDERED |
        ARRAY_ALTERNATE | ARRAY_ALT_TEXT) [1] = "MCZ-ENT00597110"	(0x50 :
        HAS_QUALIFIER | HAS_LANGUAGE) ?xml:lang = "x-default"	(0x20 :
        QUALIFIER)
             */
                        XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
                        try {
                            XMPProperty descriptionProp = xmpMeta.getArrayItem(
                                    "http://purl.org/dc/elements/1.1/", "dc:description", 1);
                            if (descriptionProp != null) {
                                String description = descriptionProp.getValue();
                                log.debug("Debug {}", description);
                                if (description != null && description.trim().length() > 0) {
                                    exifComment = description;
                                }
                            }
                        } catch (NullPointerException | XMPException e1) {
                            log.error(e1.getMessage(), e1);
                        }
                    }
                } catch (JpegProcessingException e1) {
                    log.debug(e1.getMessage(), e1);
                } catch (IOException e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
            // cache the comment if one was found, otherwise an empty string.
            exifCommentText = exifComment;
        }

        return exifCommentText;
    }

    /**
     * Get the Metatdata: created from this file
     *
     * @param format the format of the desired date
     * @return the date, formated as specified by parameter format, or null if
     * none is found
     */
    public String getExifDateCreated(String format) {
        // Actual read attempt is only invoked once,
        // subsequent calls return cached value.
        if (dateCreated == null) {
            // read, or re-read the file for a comment
            try {
                Metadata metadata = JpegMetadataReader.readMetadata(candidateFile);
                // [Exif] date
                Directory exifDirectory =
                        metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                dateCreated = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME);
                if (dateCreated == null) {
                    dateCreated =
                            exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                }
                if (dateCreated == null) {
                    dateCreated =
                            exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
                }
                log.debug("Exif DateTime = " +
                        SimpleDateFormat.getDateInstance().format(dateCreated));
            } catch (JpegProcessingException e2) {
                log.error("Error reading exif metadata.");
                log.error(e2.getMessage());
            } catch (IOException e) {
                log.error("Error reading file for exif metadata.");
                log.error(e.getMessage());
            }
        }
        if (dateCreated != null) {
            // date format shown on the media bulkloader example page.
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(dateCreated);
        }
        return null;
    }

    /**
     * Get the Metatdata: created from this file
     *
     * @return the created date, formated dd MMMMM yyyy
     */
    public String getExifDateCreated() {
        return getExifDateCreated("dd MMMMM yyyy");
    }

    /**
     * Get the identifying barcode
     *
     * @return the barcode/identifier
     */
    public String getBarcodeTextAtFoundTemplate() {
        return getBarcodeText(this.templateUsed);
    }

    /**
     * Scan part of an image for a catalogNumberBarcode, as specified by a the
     * BarcodePosition of a PositionTemplate for a QR Code barcode.
     *
     * @param positionTemplate the position template to use to identify the part
     *                         of the image that may contain
     *                         a barcode.
     * @return a text string representing the content of the barcode, if any.
     * Check for error states with a call to getBarcodeStatus();
     */
    public String getBarcodeText(PositionTemplate positionTemplate) {

        log.debug("Debug {}", catalogNumberBarcodeText);
        log.debug("Debug {}", catalogNumberBarcodeStatus);
        if (catalogNumberBarcodeText != null &&
                catalogNumberBarcodeText.trim().length() > 0 &&
                catalogNumberBarcodeStatus == RESULT_BARCODE_SCANNED) {
            return catalogNumberBarcodeText;
        }

        String returnValue = null;
        if (positionTemplate.getTemplateId().equals(
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
            // Check the entire image for a barcode and return.
            returnValue = getBarcodeText();
        } else {
            // Check the part of the image specified by the template for the barcode.
            BufferedImage image = null;
            String error = "";
            barcodeStatus = RESULT_ERROR;
            try {
                image = ImageIO.read(candidateFile);
            } catch (IOException e) {
                log.error("Error", e);
            }
            if (image == null) {
                barcodeStatus = RESULT_ERROR;
                log.error("Image null. Could not decode...");
            } else {
                if (image.getWidth() > positionTemplate.getBarcodeULPosition().width &&
                        image.getWidth() ==
                                Math.round(positionTemplate.getImageSize().getWidth())) {
                    // image might plausibly match template
                    int left = positionTemplate.getBarcodeULPosition()
                            .width; //* @param left x coordinate of leftmost pixels
                    // to decode
                    int top = positionTemplate.getBarcodeULPosition()
                            .height; //* @param top y coordinate of topmost pixels
                    // to decode
                    //*  pixels whose x coordinate is in [left,right)
                    int width = positionTemplate.getBarcodeSize().width;
                    int height = positionTemplate.getBarcodeSize().height;
                    returnValue =
                            readBarcodeFromLocation(image, left, top, width, height, false);
                    if (returnValue != null && returnValue.length() > 0) {
                        barcodeStatus = RESULT_BARCODE_SCANNED;
                    } else {
                        // Try Python fallback if templated location failed
                        log.debug("Attempting Python QR fallback for templated location");
                        List<String> pythonResults = tryPythonQRCodeFallback(image);
                        if (!pythonResults.isEmpty()) {
                            processPythonQRResults(pythonResults);
                            // Use the appropriate barcode for the template context
                            returnValue = findBestBarcodeForTemplate(pythonResults);
                            if (returnValue != null) {
                                barcodeStatus = RESULT_BARCODE_SCANNED;
                            } else {
                                returnValue = "Failed to read a barcode from templated location.";
                                barcodeStatus = RESULT_ERROR;
                            }
                        } else {
                            returnValue = "Failed to read a barcode from templated location.";
                            barcodeStatus = RESULT_ERROR;
                        }
                    }
                    log.debug("Debug {}", returnValue);
                    log.debug("barcodeStatus=" + barcodeStatus);
                } else {
                    // image is narrower than templated area.
                    returnValue = "Image is different size from Template.";
                    log.debug("ImageWidth=" + image.getWidth() + "; TemplateWidth=" +
                            Math.round(positionTemplate.getImageSize().getWidth()));
                }
            }
        }
        catalogNumberBarcodeStatus = barcodeStatus;
        if (barcodeStatus == RESULT_BARCODE_SCANNED) {
            catalogNumberBarcodeText = returnValue;
        }
        return returnValue;
    }

    /**
     * Scan the entire image for any QR Code barcode (could find a taxon barcode
     * or a catalog number barcode). Check for error states with a call to
     * getBarcodeStatus().  Does not store or set the state with any read barcode
     * value.
     *
     * @return a string representing the text of the barcode, if any.
     */
    public String getBarcodeText() {
        String returnValue;
        BufferedImage image = null;
        barcodeStatus = RESULT_ERROR;
        try {
            image = ImageIO.read(candidateFile);
        } catch (IOException e) {
            log.error("Error", e);
        }
        if (image == null) {
            returnValue = null;
            log.error("Image could not be decoded: is null");
            barcodeStatus = RESULT_ERROR;
        } else {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            try {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                returnValue = getQRCodeText(bitmap);
                barcodeStatus = RESULT_BARCODE_SCANNED;
            } catch (Exception e) {
                returnValue = null;
                log.error("Error", e);
                barcodeStatus = RESULT_ERROR;
                
                // Try Python fallback if Java-based QR reading failed
                log.debug("Attempting Python QR fallback");
                List<String> pythonResults = tryPythonQRCodeFallback(image);
                if (!pythonResults.isEmpty()) {
                    processPythonQRResults(pythonResults);
                    // Return the first found code for backward compatibility
                    returnValue = pythonResults.get(0);
                    barcodeStatus = RESULT_BARCODE_SCANNED;
                } else {
                    log.debug("Python QR fallback also failed");
                }
            }
        }
        return returnValue;
    }

    /**
     * Obtain the result of the most recent attempt to check this image for any
     * barcode. Use with caution, may not return what you think is the most recent
     * attempt.
     *
     * @return int one of RESULT_ERROR, RESULT_NOT_CHECKED, or
     * RESULT_BARCODE_SCANNED
     */
    public int getBarcodeStatus() {
        // TODO: Replace this method and variable with a structured object to return
        //   directly from barcode scanning calls.
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
     * @param templateUsed the templateUsed to set
     */
    public void setTemplateUsed(PositionTemplate templateUsed) {
        this.templateUsed = templateUsed;
    }

    /**
     * Fallback method to use Python qreader for QR code detection.
     * This method applies contrast enhancement similar to the QRCodeDiagnosticTest
     * and then uses the Python qrscan_using_python.py script to detect QR codes.
     *
     * @param image The BufferedImage to process
     * @return A list of detected QR code texts, or empty list if none found
     */
    private List<String> tryPythonQRCodeFallback(BufferedImage image) {
        List<String> results = new ArrayList<>();
        
        try {
            // Check if Python is available
            if (!isPythonAvailable()) {
                log.debug("Python not available, skipping Python QR fallback");
                return results;
            }
            
            // Scale and enhance contrast similar to QRCodeDiagnosticTest
            BufferedImage processedImage = scaleAndEnhanceContrast(image);
            
            // Create temporary file for the processed image
            Path tempImagePath = Files.createTempFile("qr_scan_", ".jpg");
            try {
                ImageIO.write(processedImage, "jpg", tempImagePath.toFile());
                
                // Get the path to the Python script
                String pythonScriptPath = findPythonScript();
                if (pythonScriptPath == null) {
                    log.debug("Python QR script not found");
                    return results;
                }
                
                // Execute Python script
                ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, tempImagePath.toString());
                Process process = pb.start();
                
                // Read the output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    // Parse JSON output
                    results = parseQRCodeResults(output.toString());
                    log.debug("Python QR fallback found {} codes", results.size());
                } else {
                    log.debug("Python QR script exited with code: {}", exitCode);
                }
                
            } finally {
                // Clean up temporary file
                Files.deleteIfExists(tempImagePath);
            }
            
        } catch (Exception e) {
            log.debug("Python QR fallback failed: {}", e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Scale and enhance contrast of the image similar to QRCodeDiagnosticTest
     */
    private BufferedImage scaleAndEnhanceContrast(BufferedImage image) {
        // Scale image to at most 750px width or height
        int maxDimension = 750;
        BufferedImage scaledImage = image;
        
        if (image.getWidth() > maxDimension || image.getHeight() > maxDimension) {
            double scale = Math.min((double) maxDimension / image.getWidth(), 
                                   (double) maxDimension / image.getHeight());
            int newWidth = (int) (image.getWidth() * scale);
            int newHeight = (int) (image.getHeight() * scale);
            scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            scaledImage.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);
        }
        
        // Enhance contrast
        BufferedImage contrastImage = new BufferedImage(scaledImage.getWidth(), scaledImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < scaledImage.getHeight(); y++) {
            for (int x = 0; x < scaledImage.getWidth(); x++) {
                int rgb = scaledImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                if ((r + g + b) > 3 * 180) {
                    r = (int) (r * 1.2); // Lighten bright pixels
                    g = (int) (g * 1.2);
                    b = (int) (b * 1.2);
                } else {
                    r = (int) (r * 0.8); // Darken dark pixels
                    g = (int) (g * 0.8);
                    b = (int) (b * 0.8);
                }
                
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                contrastImage.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        
        return contrastImage;
    }
    
    /**
     * Check if Python is available on the system
     */
    private boolean isPythonAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Find the Python QR scanning script
     */
    private String findPythonScript() {
        // Try to find the script in the resources folder
        String[] possiblePaths = {
            "src/main/resources/qrscan_using_python.py",
            "qrscan_using_python.py",
            System.getProperty("user.dir") + "/src/main/resources/qrscan_using_python.py"
        };
        
        for (String path : possiblePaths) {
            if (Files.exists(Paths.get(path))) {
                return path;
            }
        }
        
        return null;
    }
    
    /**
     * Parse the JSON output from the Python script
     */
    private List<String> parseQRCodeResults(String jsonOutput) {
        List<String> results = new ArrayList<>();
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonOutput);
            
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    if (node.isTextual()) {
                        String text = node.asText();
                        if (text != null && !text.trim().isEmpty()) {
                            results.add(text);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Failed to parse JSON output: {}", e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Process QR codes found by Python fallback and set appropriate fields
     */
    private void processPythonQRResults(List<String> qrCodes) {
        String jsonCode = null;
        String ethCode = null;
        
        for (String code : qrCodes) {
            if (code.startsWith("{") && code.endsWith("}")) {
                // This looks like JSON data (label text)
                jsonCode = code;
            } else if (code.startsWith("ETHZ-ENT") || code.startsWith("ETH-ENT")) {
                // This looks like a catalog number
                ethCode = code;
            }
        }
        
        // Set the fields appropriately
        if (jsonCode != null) {
            this.labelText = jsonCode;
            this.unitTrayTaxonLabelTextStatus = RESULT_BARCODE_SCANNED;
        }
        
        if (ethCode != null) {
            this.catalogNumberBarcodeText = ethCode;
            this.catalogNumberBarcodeStatus = RESULT_BARCODE_SCANNED;
        }
    }
    
    /**
     * Find the best barcode from Python results for the current template context
     */
    private String findBestBarcodeForTemplate(List<String> qrCodes) {
        // Prefer ETH-ENT codes for catalog number contexts
        for (String code : qrCodes) {
            if (code.startsWith("ETHZ-ENT") || code.startsWith("ETH-ENT")) {
                return code;
            }
        }
        // Fall back to first found code
        return qrCodes.isEmpty() ? null : qrCodes.get(0);
    }

    /**
     * Utility inner class to carry text and status values from barcode reader
     * methods.
     */
    private class TextStatus {
        String text;
        int status;

        /**
         * @param text
         * @param status
         */
        public TextStatus(String text, int status) {
            super();
            this.text = text;
            this.status = status;
        }

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * @return the status
         */
        public int getStatus() {
            return status;
        }

        /**
         * @param status the status to set
         */
        public void setStatus(int status) {
            this.status = status;
        }
    }
}
