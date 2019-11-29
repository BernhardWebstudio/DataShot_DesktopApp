package edu.harvard.mcz.imagecapture.jobs;

import edu.harvard.mcz.imagecapture.*;
import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.*;
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle;
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

abstract public class AbstractFileScanJob implements RunnableJob, Runnable {


    private static final Log log = LogFactory.getLog(AbstractFileScanJob.class);
    protected int percentComplete = 0;
    protected ArrayList<RunnerListener> listeners = new ArrayList<>();

    public AbstractFileScanJob() {
    }

    /**
     * Log errors associated with confusion between exifComment & detected barcode
     *
     * @param counter                to apply the error to
     * @param filename               the errored file
     * @param barcode                the barcode as detected
     * @param exifComment            the comment, exif as read from file
     * @param parser                 the parser used
     * @param barcodeInImageMetadata whether the barcode was extracted from metadata
     * @param log
     */
    static void logMismatch(Counter counter, String filename, String barcode, String exifComment, TaxonNameReturner parser, boolean barcodeInImageMetadata, Log log) {
        if (barcodeInImageMetadata || Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_REDUNDANT_COMMENT_BARCODE).equals("true")) {
            // If so configured, or if image metadata contains a barcode that doesn't match the barcode in the image
            // report on barcode/comment missmatch as an error condition.
            try {
                RunnableJobError error = new RunnableJobError(filename, barcode,
                        barcode, exifComment, "Barcode/Comment mismatch.",
                        parser, (DrawerNameReturner) parser,
                        null, RunnableJobError.TYPE_MISMATCH);
                counter.appendError(error);
            } catch (Exception e) {
                // we don't want an exception to stop processing
                log.error(e);
            }
        } else {
            // Just write into debug log
            // This would normally the case where the image metadata doesn't contain a barcode but the image does, and reporting of this state as an error has been turned off.
            log.debug("Barcode/Comment mismatch: [" + barcode + "]!=[" + exifComment + "]");
        }
    }

    /**
     * Do actual processing of one file
     *
     * @param containedFile the file to process
     * @param counter       the counter to keep track of sucess/failure
     */
    protected void checkFile(File containedFile, Counter counter) {
        Singleton.getSingletonInstance().getProperties().getProperties().setProperty(ImageCaptureProperties.KEY_LASTPATH, containedFile.getPath());
        String filename = containedFile.getName();
        counter.incrementFilesSeen();
        log.debug("Checking image file: " + filename);
        CandidateImageFile.debugCheckHeightWidth(containedFile);
        // scan file for barcodes and ocr of unit tray label text
        CandidateImageFile scannableFile = null;
        try {
            // PositionTemplateDetector detector = new ConfiguredBarcodePositionTemplateDetector();
            boolean isSpecimenImage = false;
            boolean isDrawerImage = false;
            boolean reattach = false;  // image is detached instance and should be reattached instead of persisted denovo.
            // try {
            // Check for an existing image record.
            ICImageLifeCycle imageLifeCycle = new ICImageLifeCycle();
            ICImage existingTemplate = new ICImage();
            existingTemplate.setFilename(filename);
            String path = ImageCaptureProperties.getPathBelowBase(containedFile);
            existingTemplate.setPath(path);
            List<ICImage> matches = imageLifeCycle.findBy(new HashMap<String, Object>() {{
                put("path", path);
                put("filename", filename);
            }});
            log.debug(matches != null ? matches.size() : "no matches found");
            if (matches != null && matches.size() == 1
                    && matches.get(0).getRawBarcode() == null
                    && matches.get(0).getRawExifBarcode() == null
                    && (matches.get(0).getDrawerNumber() == null || matches.get(0).getDrawerNumber().trim().length() == 0)
            ) {
                // likely case for a failure to read data out of the image file
                // try to update the image file record.
                try {
                    existingTemplate = imageLifeCycle.merge(matches.get(0));
                    matches.remove(0);
                    reattach = true;
                    log.debug(existingTemplate);
                } catch (SaveFailedException e) {
                    log.error(e.getMessage(), e);
                }
            } else if (matches != null && matches.size() == 1 && matches.get(0).getSpecimen() == null) {
                // likely case for a failure to create a specimen record in a previous run
                // try to update the image file record
                try {
                    existingTemplate = imageLifeCycle.merge(matches.get(0));
                    matches.remove(0);
                    reattach = true;
                    log.debug(existingTemplate);
                } catch (SaveFailedException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (matches == null || matches.size() == 0) {
                createDatabaseRecordForFile(containedFile, counter, reattach, imageLifeCycle, existingTemplate);
            } else {
                // found an already databased file (where we have barcode/specimen or drawer number data).
                log.debug("Record exists, skipping file " + filename);
                counter.incrementFilesExisting();
            }
            // } catch (NoSuchTemplateException e) {
            //	log.error("Detected Template for image doesn't exist. " + e.getMessage());
            //}


        } catch (UnreadableFileException e) {
            counter.incrementFilesFailed();
            counter.appendError(new RunnableJobError(containedFile.getName(), "", "Could not read file", new UnreadableFileException(), RunnableJobError.TYPE_FILE_READ));
            log.error("Couldn't read file." + e.getMessage());
            //} catch (OCRReadException e) {
            //	counter.incrementFilesFailed();
            //	log.error("Couldn't OCR file." + e.getMessage());
        }
    }

    /**
     * Create a new image database record
     *
     * @param containedFile  the file path relative to the start
     * @param counter        to count errors
     * @param reattach       whether there is already a database record existing, to be overridden
     * @param imageLifeCycle the repository to sage to
     * @param image          providing access to path & filename
     * @throws UnreadableFileException
     */
    protected void createDatabaseRecordForFile(File containedFile, Counter counter, boolean reattach, ICImageLifeCycle imageLifeCycle, ICImage image) throws UnreadableFileException {
        boolean isSpecimenImage = false, isDrawerImage = false;
        BarcodeMatcher matcher = Singleton.getSingletonInstance().getBarcodeMatcher();
        // ** Identify the template.
        // String templateId = detector.detectTemplateForImage(fileToCheck);
        // log.debug("Detected Template: " + templateId);
        // PositionTemplate template = new PositionTemplate(templateId);
        // // Found a barcode in a templated position in the image.
        // // ** Scan the file based on this template.
        // scannableFile = new CandidateImageFile(fileToCheck, template);

        // Construct a CandidateImageFile with constructor that self detects template
        CandidateImageFile scannableFile = new CandidateImageFile(containedFile);
        PositionTemplate template = scannableFile.getTemplateUsed();
        String templateId = template.getName();
        log.debug("Detected Template: " + templateId);
        log.debug(scannableFile.getCatalogNumberBarcodeStatus());
        String barcode = scannableFile.getBarcodeTextAtFoundTemplate();
        if (scannableFile.getCatalogNumberBarcodeStatus() != CandidateImageFile.RESULT_BARCODE_SCANNED) {
            log.error("Error scanning for barcode: " + barcode);
            barcode = "";
        }
        log.debug("Barcode: " + barcode);
        String exifComment = scannableFile.getExifUserCommentText();
        log.debug("ExifComment: " + exifComment);
        UnitTrayLabel labelRead = scannableFile.getTaxonLabelQRText(template);
        TaxonNameReturner parser;
        String rawOCR;
        String state;

        if (labelRead != null) {
            rawOCR = labelRead.toJSONString();
            log.debug("UnitTrayLabel: " + rawOCR);
            state = WorkFlowStatus.STAGE_1;
            parser = labelRead;
        } else {
            try {
                rawOCR = scannableFile.getTaxonLabelOCRText(template);
            } catch (OCRReadException e) {
                log.error(e);
                rawOCR = "";
                log.error("Couldn't OCR file." + e.getMessage());
                RunnableJobError error = new RunnableJobError(image.getFilename(), "OCR Failed",
                        barcode, exifComment, "Couldn't find text to OCR",
                        null, null,
                        e, RunnableJobError.TYPE_NO_TEMPLATE);
                counter.appendError(error);
            }
            if (rawOCR == null) {
                rawOCR = "";
            }
            state = WorkFlowStatus.STAGE_0;
            parser = new UnitTrayLabelParser(rawOCR);
            // Provide error message to distinguish between entirely OCR or
            if (((UnitTrayLabelParser) parser).isParsedFromJSON()) {
                RunnableJobError error = new RunnableJobError(image.getFilename(), "OCR Failover found barcode.",
                        barcode, exifComment, "Couldn't read Taxon barcode, failed over to OCR, but OCR found taxon barcode.",
                        parser, null,
                        null, RunnableJobError.TYPE_FAILOVER_TO_OCR);
                counter.appendError(error);
            } else {
                RunnableJobError error = new RunnableJobError(image.getFilename(), "TaxonLabel read failed.",
                        barcode, exifComment, "Couldn't read Taxon barcode, failed over to OCR only.",
                        parser, null,
                        null, RunnableJobError.TYPE_FAILOVER_TO_OCR);
                counter.appendError(error);
            }
        }

        // Test: is exifComment a barcode:

        // Case 1: This is an image of papers associated with a container (a unit tray or a box).
        // This case can be identified by there being no barcode data associated with the image.
        // Action:
        // A) Check the exifComment to see what metadata is there, if blank, User needs to fix.
        //    exifComment may contain a drawer number, identifying this as a drawer image.  Save as such.
        // Options: A drawer, for which number is captured.  A unit tray, capture ?????????.  A specimen
        // where barcode wasn't read, allow capture of barcode and treat as Case 2.
        // B) Create an image record and store the image metadata (with a null specimen_id).

        // Case 2: This is an image of a specimen and associated labels or an image assocated with
        // a specimen with the specimen's barcode label in the image.
        // This case can be identified by there being a barcode in a templated position or there
        // being a barcode in the exif comment tag.
        // Action:
        // A) Check if a specimen record exists, if not, create one from the barcode and OCR data.
        // B) Create an image record and store the image metadata.

        if (matcher.matchesPattern(exifComment)
                || matcher.matchesPattern(barcode)) {
            isSpecimenImage = true;
            System.out.println("Specimen Image");
        } else {
            if (exifComment != null && exifComment.matches(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER))) {
                isDrawerImage = true;
                System.out.println("Drawer Image");
            } else {
                if (templateId.equals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
                    log.debug("Image doesn't appear to contain a barcode in a templated position.");
                    RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                            barcode, exifComment, "Image doesn't appear to contain a barcode in a templated position.",
                            null, null,
                            null, RunnableJobError.TYPE_NO_TEMPLATE);
                    counter.appendError(error);
                    counter.incrementFilesFailed();
                } else {
                    // Nothing found.  Need to ask.
                    RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                            barcode, exifComment, "Image doesn't appear to contain a barcode in a templated position.",
                            null, null,
                            null, RunnableJobError.TYPE_UNKNOWN);
                    counter.appendError(error);
                    counter.incrementFilesFailed();
                }
            }
        }

        image.setRawBarcode(barcode);
        if (isSpecimenImage) {
            createDatabaseRecordForSpecimen(containedFile, counter, image, barcode, exifComment, parser, labelRead, state);
        }
        if (isDrawerImage) {
            image.setDrawerNumber(exifComment);
        } else {
            image.setRawExifBarcode(exifComment);
            image.setDrawerNumber(((DrawerNameReturner) parser).getDrawerNumber());
        }
        image.setRawOcr(rawOCR);
        image.setTemplateId(template.getTemplateId());
        image.setPath(image.getPath());
        // Create md5hash of image file, persist with image
        if (image.getMd5sum() == null || image.getMd5sum().length() == 0) {
            try {
                image.setMd5sum(DigestUtils.md5Hex(new FileInputStream(containedFile)));
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        try {
            if (reattach) {
                // Update image file record
                imageLifeCycle.attachDirty(image);
                log.debug("Updated " + image.toString());
                counter.incrementFilesUpdated();
            } else {
                // *** Save a database record of the image file.
                imageLifeCycle.persist(image);
                log.debug("Saved " + image.toString());
                counter.incrementFilesDatabased();
            }
        } catch (SaveFailedException e) {
            log.error(e.getMessage(), e);
            counter.incrementFilesFailed();
            String failureMessage = "Failed to save image record.  " + e.getMessage();
            RunnableJobError error = new RunnableJobError(image.getFilename(), "Save Failed",
                    image.getFilename(), image.getPath(), failureMessage,
                    null, null,
                    null, RunnableJobError.TYPE_SAVE_FAILED);
            counter.appendError(error);
        }
        if (isSpecimenImage) {
            if (Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_REDUNDANT_COMMENT_BARCODE).equals("true")) {
                // If so configured, log as error
                if (!image.getRawBarcode().equals(image.getRawExifBarcode())) {
                    log.error("Warning: Scanned Image has missmatch between barcode and comment.");
                }
            }
        }
    }

    /**
     * Create or update an existing Specimen record to associate it with the ICImage
     *
     * @param containedFile the file of the image
     * @param counter       counter to add errors
     * @param image         the image to associate with the specimen
     * @param barcode       the barcode associated with the specimen
     * @param exifComment   the exif metadata comment to enable additional data extraction
     * @param unitTrayLabel the unit tray label of the Specimen
     * @param state         the workflow state we are in with the specimen/image
     */
    protected void createDatabaseRecordForSpecimen(File containedFile, Counter counter, ICImage image, String barcode, final String exifComment, TaxonNameReturner parser, UnitTrayLabel unitTrayLabel, String state) {
        BarcodeMatcher matcher = Singleton.getSingletonInstance().getBarcodeMatcher();
        final String rawBarcode = barcode;
        if (!rawBarcode.equals(exifComment)) {
            // Log the missmatch
            logMismatch(counter, image.getFilename(), barcode, exifComment, parser, matcher.matchesPattern(exifComment), log);
        }

        Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Creating new specimen record.");
        Specimen s = new Specimen();
        if ((!matcher.matchesPattern(barcode))
                && matcher.matchesPattern(exifComment)) {
            // special case: couldn't read QR code barcode from image, but it was present in exif comment.
            s.setBarcode(exifComment);
            barcode = exifComment;
        } else {
            if (!matcher.matchesPattern(barcode)) {
                // Won't be able to save the specimen record if we end up here.
                log.error("Neither exifComment nor QR Code barcode match the expected pattern for a barcode, but isSpecimenImage got set to true.");
            }
            s.setBarcode(barcode);
        }

        // check if there already exists a specimen to add the image to
        SpecimenLifeCycle specimenLifeCycle = new SpecimenLifeCycle();
        List<Specimen> existingSpecimens = specimenLifeCycle.findByBarcode(s.getBarcode());
        if (existingSpecimens != null && existingSpecimens.size() > 0) {
            counter.incrementSpecimenExisting();
            assert (existingSpecimens.size() == 1);
            for (Specimen specimen : existingSpecimens) {
                image.setSpecimen(specimen);
                specimen.getICImages().add(image);
                try {
                    specimenLifeCycle.attachDirty(specimen);
                } catch (SaveFailedException e) {
                    counter.appendError(new RunnableJobError(containedFile.getName(), barcode, specimen.getSpecimenId().toString(), "Failed adding image to existing Specimen", e, RunnableJobError.TYPE_SAVE_FAILED));
                }
            }
            return;
        }

        s.setWorkFlowStatus(state);

        if (unitTrayLabel != null) {
            //  We got json data from a barcode.
            s.setFamily(parser.getFamily());
            s.setSubfamily(parser.getSubfamily());
            s.setTribe(parser.getTribe());
        } else {
            // We failed over to OCR, try lookup in DB.
            s.setFamily("");  // make sure there's a a non-null value in family.
            if (parser.getTribe().trim().equals("")) {
                HigherTaxonLifeCycle hls = new HigherTaxonLifeCycle();
                if (hls.isMatched(parser.getFamily(), parser.getSubfamily())) {
                    // If there is a match, use it.
                    String[] higher = hls.findMatch(parser.getFamily(), parser.getSubfamily());
                    s.setFamily(higher[0]);
                    s.setSubfamily(higher[1]);
                } else {
                    // otherwise use the raw OCR output.
                    s.setFamily(parser.getFamily());
                    s.setSubfamily(parser.getSubfamily());
                }
                s.setTribe("");
            } else {
                HigherTaxonLifeCycle hls = new HigherTaxonLifeCycle();
                if (hls.isMatched(parser.getFamily(), parser.getSubfamily(), parser.getTribe())) {
                    String[] higher = hls.findMatch(parser.getFamily(), parser.getSubfamily(), parser.getTribe());
                    s.setFamily(higher[0]);
                    s.setSubfamily(higher[1]);
                    s.setTribe(higher[2]);
                } else {
                    s.setFamily(parser.getFamily());
                    s.setSubfamily(parser.getSubfamily());
                    s.setTribe(parser.getTribe());
                }
            }
        }
        if (state.equals(WorkFlowStatus.STAGE_0)) {
            // Look up likely matches for the OCR of the higher taxa in the HigherTaxon authority file.
            if (!parser.getFamily().equals("")) {
                // check family against database (with a soundex match)
                HigherTaxonLifeCycle hls = new HigherTaxonLifeCycle();
                String match = hls.findMatch(parser.getFamily());
                if (match != null && !match.trim().equals("")) {
                    s.setFamily(match);
                }
            }
        }
        // trim family to fit (in case multiple parts of taxon name weren't parsed
        // and got concatenated into family field.
        JobSingleBarcodeScan.setBasicSpecimenFromParser(parser, s);
        s.setCreatingPath(ImageCaptureProperties.getPathBelowBase(containedFile));
        s.setCreatingFilename(containedFile.getName());
        if (parser.getIdentifiedBy() != null && parser.getIdentifiedBy().length() > 0) {
            s.setIdentifiedBy(parser.getIdentifiedBy());
        }
        log.debug(s.getCollection());

        s.setCreatedBy(ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.getAppVersion());
        s.setDateCreated(new Date());
        try {
            // *** Save a database record of the specimen.
            specimenLifeCycle.persist(s);
            counter.incrementSpecimenDatabased();
            s.attachNewPart();
        } catch (SpecimenExistsException e) {
            log.debug(e.getMessage());
            // Expected case on scanning a second image for a specimen.
            // Doesn't need to be reported as a parsing error.
            //
            // Look up the existing record to link this specimen to it.
            try {
                List<Specimen> checkResult = specimenLifeCycle.findByBarcode(barcode);
                if (checkResult.size() == 1) {
                    s = checkResult.get(0);
                }
            } catch (Exception e2) {
                s = null; // so that saving the image record doesn't fail on trying to save linked transient specimen record.
                String errorMessage = "Linking Error: \nFailed to link image to existing specimen record.\n";
                RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                        rawBarcode, exifComment, errorMessage,
                        parser, (DrawerNameReturner) parser,
                        e2, RunnableJobError.TYPE_SAVE_FAILED);
                counter.appendError(error);
            }
        } catch (SaveFailedException e) {
            // Couldn't save for some reason other than the
            // specimen record already existing.  Check for possible
            // save problems resulting from parsing errors.
            log.error(e);
            try {
                List<Specimen> checkResult = specimenLifeCycle.findByBarcode(barcode);
                if (checkResult.size() == 1) {
                    s = checkResult.get(0);
                }
                // Drawer number with length limit (and specimen that fails to save at over this length makes
                // a good canary for labels that parse very badly.
                if (((DrawerNameReturner) parser).getDrawerNumber().length() > MetadataRetriever.getFieldLength(Specimen.class, "DrawerNumber")) {
                    String badParse = "";
                    badParse = "Parsing problem. \nDrawer number is too long: " + s.getDrawerNumber() + "\n";
                    RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                            rawBarcode, exifComment, badParse,
                            parser, (DrawerNameReturner) parser,
                            e, RunnableJobError.TYPE_BAD_PARSE);
                    counter.appendError(error);
                } else {
                    RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                            rawBarcode, exifComment, e.getMessage(),
                            parser, (DrawerNameReturner) parser,
                            e, RunnableJobError.TYPE_SAVE_FAILED);
                    counter.appendError(error);

                }
            } catch (Exception err) {
                log.error(err);

                String badParse = "";
                // Drawer number with length limit (and specimen that fails to save at over this length makes
                // a good canary for labels that parse very badly.
                if (s.getDrawerNumber() == null) {
                    badParse = "Parsing problem. \nDrawer number is null: \n";
                } else {
                    if (s.getDrawerNumber().length() > MetadataRetriever.getFieldLength(Specimen.class, "DrawerNumber")) {
                        // This was an OK test for testing OCR, but in production ends up in records not being
                        // created for files, which ends up being a larger quality control problem than records
                        // with bad OCR.

                        // Won't fail this way anymore - drawer number is now enforced in Specimen.setDrawerNumber()
                        badParse = "Parsing problem. \nDrawer number is too long: " + s.getDrawerNumber() + "\n";
                    }
                }
                RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                        rawBarcode, exifComment, badParse,
                        parser, (DrawerNameReturner) parser,
                        err, RunnableJobError.TYPE_SAVE_FAILED);
                counter.appendError(error);
                counter.incrementFilesFailed();
                s = null;
            }
        } catch (Exception ex) {
            log.error(ex);
            RunnableJobError error = new RunnableJobError(image.getFilename(), barcode,
                    rawBarcode, exifComment, ex.getMessage(),
                    parser, (DrawerNameReturner) parser,
                    ex, RunnableJobError.TYPE_SAVE_FAILED);
            counter.appendError(error);
        }
        if (s != null) {
            image.setSpecimen(s);
        }
    }

    /**
     * Set the completeness percentage in main frame & notify listeners
     *
     * @param aPercentage
     */
    protected void setPercentComplete(final int aPercentage) {
        //set value
        percentComplete = aPercentage;
        //notify listeners
        Singleton.getSingletonInstance().getMainFrame().notifyListener(percentComplete, this);
        for (RunnerListener listener : listeners) {
            listener.notifyListener(percentComplete, this);
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        start();
    }

    /**
     * Cleanup when job is complete.
     */
    protected void done() {
        Singleton.getSingletonInstance().getJobList().removeJob(this);
    }
}
