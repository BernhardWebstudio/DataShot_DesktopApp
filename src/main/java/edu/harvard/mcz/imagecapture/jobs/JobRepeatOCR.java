/**
 * JobRepeatOCR.java
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
package edu.harvard.mcz.imagecapture.jobs;

import edu.harvard.mcz.imagecapture.*;
import edu.harvard.mcz.imagecapture.data.LocationInCollection;
import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.data.RunStatus;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner;
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob;
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener;
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner;
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * JobRepeatOCR
 */
public class JobRepeatOCR implements RunnableJob, Runnable {

    /**
     * Scan all of image base directory tree.
     */
    public static final int SCAN_ALL = 0;
    /**
     * Open a dialog and scan a specific directory.
     */
    public static final int SCAN_SELECT = 1;
    /**
     * From a file, scan a specific directory.
     */
    public static final int SCAN_SPECIFIC = 2;
    private static final Logger log = LoggerFactory.getLogger(JobRepeatOCR.class);
    Counter counter = null; // For reporting results
    ArrayList<RunnerListener> listeners = null;
    private int scan = SCAN_ALL;            // default scan all
    private File startPointSpecific = null; // place to start for scan_specific
    private int runStatus = RunStatus.STATUS_NEW;
    private Date startDate = null;
    private int percentComplete = 0;

    /**
     * Default constructor.  Creates an OCR job to repeat the OCR on all
     * images for all specimens in state OCR.
     */
    public JobRepeatOCR() {
        scan = SCAN_ALL;
        startPointSpecific = null;
        runStatus = RunStatus.STATUS_NEW;
        init();
    }

    /**
     * Create a repeat OCR job to bring up dialog to pick a specific directory
     * on which to repeat OCR for specimens in state OCR or to repeat OCR
     * for a specific directory specified by startAt, again for specimens in
     * state OCR.
     * <p>
     * Behavior:
     * <p>
     * whatToScan=SCAN_ALL, startAt is ignored, equivalent to default constructor.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory
     * chooser dialog. whatToScan=SCAN_SPECIFIC, startAt is used as starting point
     * for repeat (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_ALL, SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    public JobRepeatOCR(int whatToScan, File startAt) {
        scan = SCAN_SELECT;
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan
        // if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt;
        }
        if (whatToScan == SCAN_ALL) {
            // equivalent to default constructor
            scan = SCAN_ALL;
            startPointSpecific = null;
        }
        if (whatToScan == SCAN_SPECIFIC) {
            if ((startAt != null) && startAt.canRead()) {
                scan = SCAN_SPECIFIC;
            } else {
                scan = SCAN_SELECT;
            }
        }
        runStatus = RunStatus.STATUS_NEW;
        init();
    }

    protected void init() {
        listeners = new ArrayList<RunnerListener>();
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    @Override
    public boolean cancel() {
        runStatus = RunStatus.STATUS_TERMINATED;
        log.debug("JobRepeatOCR " + this + "  recieved cancel signal");
        return false;
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStatus()
     */
    @Override
    public int getStatus() {
        return runStatus;
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#percentComplete()
     */
    @Override
    public int percentComplete() {
        return percentComplete;
    }

    /* (non-Javadoc)
     * @see
     *     edu.harvard.mcz.imagecapture.interfaces.RunnableJob#registerListener(edu.harvard.mcz.imagecapture.interfaces.RunnerListener)
     */
    @Override
    public boolean registerListener(RunnerListener jobListener) {
        if (listeners == null) {
            init();
        }
        log.debug("Debug {}", jobListener);
        return listeners.add(jobListener);
    }

    private List<File> getFileList() {
        String pathToCheck = "";
        if (scan != SCAN_ALL) {
            // Find the path in which to include files.
            File imagebase = null; // place to start the scan from, imagebase
            // directory for SCAN_ALL
            File startPoint = null;
            // If it isn't null, retrieve the image base directory from properties,
            // and test for read access.
            if (Singleton.getSingletonInstance()
                    .getProperties()
                    .getProperties()
                    .getProperty(ImageCaptureProperties.KEY_IMAGEBASE) == null) {
                JOptionPane.showMessageDialog(
                        Singleton.getSingletonInstance().getMainFrame(),
                        "Can't start scan.  Don't know where images are stored.  Set imagbase property.",
                        "Can't Scan.", JOptionPane.ERROR_MESSAGE);
            } else {
                imagebase =
                        new File(Singleton.getSingletonInstance()
                                .getProperties()
                                .getProperties()
                                .getProperty(ImageCaptureProperties.KEY_IMAGEBASE));
                if (imagebase != null) {
                    if (imagebase.canRead()) {
                        startPoint = imagebase;
                    } else {
                        // If it can't be read, null out imagebase
                        imagebase = null;
                    }
                }
                if (scan == SCAN_SPECIFIC && startPointSpecific != null &&
                        startPointSpecific.canRead()) {
                    // A scan start point has been provided, don't launch a dialog.
                    startPoint = startPointSpecific;
                }
                if (imagebase == null || scan == SCAN_SELECT) {
                    // launch a file chooser dialog to select the directory to scan
                    final JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (scan == SCAN_SELECT && startPointSpecific != null &&
                            startPointSpecific.canRead()) {
                        fileChooser.setCurrentDirectory(startPointSpecific);
                    } else {
                        if (Singleton.getSingletonInstance()
                                .getProperties()
                                .getProperties()
                                .getProperty(ImageCaptureProperties.KEY_LASTPATH) != null) {
                            fileChooser.setCurrentDirectory(new File(
                                    Singleton.getSingletonInstance()
                                            .getProperties()
                                            .getProperties()
                                            .getProperty(ImageCaptureProperties.KEY_LASTPATH)));
                        }
                    }
                    int returnValue = fileChooser.showOpenDialog(
                            Singleton.getSingletonInstance().getMainFrame());
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        log.debug("Selected base directory: " + file.getName() + ".");
                        startPoint = file;
                    } else {
                        // TODO: handle error condition
                        log.error("Directory selection cancelled by user.");
                    }
                    // TODO: Filechooser to pick path, then save (if SCAN_ALL) imagebase
                    // property. Perhaps.  Might be undesirable behavior. Probably better
                    // to warn that imagebase is null;
                }

                // Check that startPoint is or is within imagebase.
                if (!ImageCaptureProperties.isInPathBelowBase(startPoint)) {
                    String base = Singleton.getSingletonInstance()
                            .getProperties()
                            .getProperties()
                            .getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
                    log.error("Tried to scan directory (" + startPoint.getPath() +
                            ") outside of base image directory (" + base + ")");
                    String message =
                            "Can't scan and database files outside of base image directory (" +
                                    base + ")";
                    JOptionPane.showMessageDialog(
                            Singleton.getSingletonInstance().getMainFrame(), message,
                            "Can't Scan outside image base directory.",
                            JOptionPane.YES_NO_OPTION);
                } else {

                    // run in separate thread and allow cancellation and status reporting

                    // walk through directory tree

                    if (!startPoint.canRead()) {
                        JOptionPane.showMessageDialog(
                                Singleton.getSingletonInstance().getMainFrame(),
                                "Can't start scan.  Unable to read selected directory: " +
                                        startPoint.getPath(),
                                "Can't Scan.", JOptionPane.YES_NO_OPTION);
                    } else {
                        pathToCheck = ImageCaptureProperties.getPathBelowBase(startPoint);
                    }
                }
            }
        }

        // Retrieve a list of all specimens in state OCR
        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        List<Specimen> specimens =
                sls.findBy("workFlowStatus", WorkFlowStatus.STAGE_0);
        ArrayList<File> files = new ArrayList<File>();
        for (int i = 0; i < specimens.size(); i++) {
            Specimen s = specimens.get(i);
            Set<ICImage> images = s.getICImages();
            Iterator<ICImage> iter = images.iterator();
            while (iter.hasNext() && runStatus != RunStatus.STATUS_TERMINATED) {
                ICImage image = iter.next();
                if (scan == SCAN_ALL || image.getPath().startsWith(pathToCheck)) {
                    // Add image for specimen to list to check
                    File imageFile = new File(ImageCaptureProperties.assemblePathWithBase(
                            image.getPath(), image.getFilename()));
                    files.add(imageFile);
                    counter.incrementFilesSeen();
                }
            }
        }
        String message =
                "Found " + files.size() + " Specimen records on which to repeat OCR.";
        log.debug("Debug {}", message);
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(message);

        return files;
    }

    private void redoOCR(File file) {
        log.debug("Debug {}", file);
        String filename = file.getName();

        String rawOCR = "";
        // scan selected file
        PositionTemplate templateToUse = null;
        // Figure out which template to use.
        DefaultPositionTemplateDetector detector =
                new DefaultPositionTemplateDetector();
        String template = "";
        try {
            template = detector.detectTemplateForImage(file);
        } catch (UnreadableFileException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        try {
            templateToUse = new PositionTemplate(template);
            log.debug("Set template to: " + templateToUse.getTemplateId());
        } catch (NoSuchTemplateException e1) {
            try {
                templateToUse = new PositionTemplate(PositionTemplate.TEMPLATE_DEFAULT);
                log.error("Template not recongised, reset template to: " +
                        templateToUse.getTemplateId());
            } catch (Exception e2) {
                // We shouldn't end up here - we just asked for the default template by
                // its constant.
                log.error("PositionTemplate doesn't recognize TEMPLATE_DEFAULT");
                log.trace("Trace", e2);
                ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR);
            }
        }

        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                "Repeat OCR " + filename + ".");

        CandidateImageFile scannableFile;
        try {
            scannableFile = new CandidateImageFile(file, templateToUse);

            try {
                // OCR and parse UnitTray Label
                TaxonNameReturner parser = null;
                UnitTrayLabel labelRead = null;
                boolean foundQRText = false;
                try {
                    labelRead = scannableFile.getTaxonLabelQRText(
                            new PositionTemplate("Test template 2"));
                } catch (NoSuchTemplateException e) {
                    try {
                        labelRead = scannableFile.getTaxonLabelQRText(
                                new PositionTemplate("Small template 2"));
                    } catch (NoSuchTemplateException e1) {
                        log.error("Neither Test template 2 nor Small template 2 found");
                    }
                }
                if (labelRead != null) {
                    rawOCR = labelRead.toJSONString();
                    foundQRText = true;
                    parser = labelRead;
                } else {
                    log.debug("Failing over to OCR with tesseract");
                    rawOCR = scannableFile.getTaxonLabelOCRText(templateToUse);
                    parser = new UnitTrayLabelParser(rawOCR);
                    foundQRText = ((UnitTrayLabelParser) parser).isParsedFromJSON();
                }
                log.debug("Debug {}", rawOCR);

                // Test this image to see if is a specimen image
                String barcode = scannableFile.getBarcodeText(templateToUse);
                Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                        "Checking " + barcode + ".");
                if (scannableFile.getCatalogNumberBarcodeStatus() !=
                        CandidateImageFile.RESULT_BARCODE_SCANNED) {
                    log.error("Error scanning for barcode: " + barcode);
                    barcode = "";
                }
                System.out.println("Barcode=" + barcode);
                String exifComment = scannableFile.getExifUserCommentText();
                boolean isSpecimenImage = false;
                if (Singleton.getSingletonInstance().getBarcodeMatcher().matchesPattern(
                        exifComment) ||
                        Singleton.getSingletonInstance().getBarcodeMatcher().matchesPattern(
                                barcode)) {
                    isSpecimenImage = true;
                    System.out.println("Specimen Image");
                }
                String rawBarcode = barcode;

                // Check for mismatch in barcode and comment
                if (!rawBarcode.equals(exifComment)) {
                    // Use the exifComment if it is a barcode
                    boolean barcodeInImageMetadata = false;
                    if (Singleton.getSingletonInstance()
                            .getBarcodeMatcher()
                            .matchesPattern(exifComment)) {
                        rawBarcode = exifComment;
                        barcodeInImageMetadata = true;
                    }
                    // Log the mismatch
                    JobAllImageFilesScan.logMismatch(counter, filename, barcode,
                            exifComment, parser,
                            barcodeInImageMetadata, log);
                }
                if (isSpecimenImage &&
                        Singleton.getSingletonInstance().getBarcodeMatcher().matchesPattern(
                                rawBarcode)) {
                    // Parse and store OCR in an updated specimen record.
                    Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                            "Updating " + barcode + ".");
                    SpecimenLifeCycle sls = new SpecimenLifeCycle();
                    Specimen specimenSearch = new Specimen();
                    specimenSearch.setBarcode(rawBarcode);
                    List<Specimen> specimens = sls.findByExample(specimenSearch);
                    log.debug("Found " + specimens.size() + " for barcode " + rawBarcode);
                    if (specimens.size() == 1) {
                        // Only update if we got a single match back on the barcode search.
                        Specimen s = specimens.get(0);
                        log.debug("Found " + s.getBarcode() + " at state " +
                                s.getWorkFlowStatus());
                        if (s.getWorkFlowStatus().equals(WorkFlowStatus.STAGE_0)) {
                            // Only update if the result was in state OCR.
                            //
                            // Look up likely matches for the OCR of the higher taxa in the
                            // HigherTaxon authority file.
                            AbstractFileScanJob.extractFamilyToSpecimen(parser, s);
                            if (!parser.getFamily().equals("")) {
                                // check family against database (with a soundex match)
                                HigherTaxonLifeCycle hls = new HigherTaxonLifeCycle();
                                String match = hls.findMatch(parser.getFamily());
                                if (match != null && !match.trim().equals("")) {
                                    s.setFamily(match);
                                }
                            }
                            // trim family to fit (in case multiple parts of taxon name
                            // weren't parsed and got concatenated into family field.
                            JobSingleBarcodeScan.setBasicSpecimenFromParser(parser, s);
                            if (s.getCreatingPath() == null ||
                                    s.getCreatingPath().length() == 0) {
                                s.setCreatingPath(
                                        ImageCaptureProperties.getPathBelowBase(file));
                            }
                            if (s.getCreatingFilename() == null ||
                                    s.getCreatingFilename().length() == 0) {
                                s.setCreatingFilename(file.getName());
                            }
                            if (parser.getIdentifiedBy() != null &&
                                    parser.getIdentifiedBy().length() > 0) {
                                s.setIdentifiedBy(parser.getIdentifiedBy());
                            }
                            log.debug("Debug {}", s.getCollection());

                            // TODO: non-general workflows
                            s.setLocationInCollection(
                                    LocationInCollection.getDefaultLocation());
                            if (s.getFamily().equals("Formicidae")) {
                                s.setLocationInCollection(LocationInCollection.GENERALANT);
                            }
                            s.setCreatedBy(ImageCaptureApp.APP_NAME + " " +
                                    ImageCaptureApp.getAppVersion());
                            SpecimenLifeCycle sh = new SpecimenLifeCycle();
                            try {
                                // *** Save a database record of the specimen.
                                log.debug("Saving changes for barcode " + barcode);
                                if (foundQRText) {
                                    // if we managed to read JSON, then we can move the specimen
                                    // to text entered.
                                    s.setWorkFlowStatus(WorkFlowStatus.STAGE_1);
                                    log.debug("Debug {}", s.getWorkFlowStatus());
                                }
                                sh.attachDirty(s);
                                counter.incrementSpecimensUpdated();
                            } catch (SaveFailedException e) {
                                // couldn't save, try to figure out why and report
                                log.debug("Debug {}", e);
                                try {
                                    String badParse = "";
                                    // Drawer number with length limit (and specimen that fails to
                                    // save at over this length makes a good canary for labels
                                    // that parse very badly.
                                    if (((DrawerNameReturner) parser).getDrawerNumber().length() >
                                            MetadataRetriever.getFieldLength(Specimen.class,
                                                    "DrawerNumber")) {
                                        badParse =
                                                "Parsing problem. \nDrawer number is too long: " +
                                                        s.getDrawerNumber() + "\n";
                                    }
                                    RunnableJobError error = new RunnableJobError(
                                            filename, barcode, rawBarcode, exifComment, badParse,
                                            parser, (DrawerNameReturner) parser, null,
                                            RunnableJobError.TYPE_BAD_PARSE);
                                    counter.appendError(error);
                                } catch (Exception err) {
                                    log.error("Error", e);
                                    log.error("Error", err);
                                    // TODO: Add a general error handling/inform user class.

                                    String badParse = "";
                                    // Drawer number with length limit (and specimen that fails to
                                    // save at over this length makes a good canary for labels
                                    // that parse very badly.
                                    if (s.getDrawerNumber() == null) {
                                        badParse = "Parsing problem. \nDrawer number is null: \n";
                                    } else {
                                        if (s.getDrawerNumber().length() >
                                                MetadataRetriever.getFieldLength(Specimen.class,
                                                        "DrawerNumber")) {
                                            // This was an OK test for testing OCR, but in production
                                            // ends up in records not being created for files, which
                                            // ends up being a larger quality control problem than
                                            // records with bad OCR.

                                            // Won't fail this way anymore - drawer number is now
                                            // enforced in Specimen.setDrawerNumber()
                                            badParse =
                                                    "Parsing problem. \nDrawer number is too long: " +
                                                            s.getDrawerNumber() + "\n";
                                        }
                                    }
                                    RunnableJobError error = new RunnableJobError(
                                            filename, barcode, rawBarcode, exifComment, badParse,
                                            parser, (DrawerNameReturner) parser, err,
                                            RunnableJobError.TYPE_SAVE_FAILED);
                                    counter.appendError(error);
                                    counter.incrementFilesFailed();
                                    s = null;
                                }
                            }
                        } else {
                            log.debug("Didn't try to save, not at workflow status OCR.");
                            RunnableJobError error = new RunnableJobError(
                                    filename, barcode, rawBarcode, exifComment,
                                    "Didn't try to save, not at workflow status OCR", parser,
                                    (DrawerNameReturner) parser, null,
                                    RunnableJobError.TYPE_SAVE_FAILED);
                            counter.appendError(error);
                        }
                    }
                } else {
                    log.debug("Didn't try to save, not a specimen image.");
                    RunnableJobError error = new RunnableJobError(
                            filename, barcode, rawBarcode, exifComment,
                            "Didn't try to save, not a specimen image, or rawBarcode doesn't match pattern",
                            parser, (DrawerNameReturner) parser, null,
                            RunnableJobError.TYPE_SAVE_FAILED);
                    counter.appendError(error);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } catch (UnreadableFileException e1) {
            log.error("Unable to read selected file." + e1.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    @Override
    public void start() {
        startDate = new Date();
        Singleton.getSingletonInstance().getJobList().addJob(this);
        counter = new Counter();
        // Obtain a list of image files to repeat OCR
        // (by querying specimens in state OCR and getting list of linked images).
        List<File> files = getFileList();
        log.debug("repeatOCRJob started" + this);
        int i = 0;
        while (i < files.size() && runStatus != RunStatus.STATUS_TERMINATED) {
            // Find out how far along the process is
            Float seen = 0.0f + i;
            Float total = 0.0f + files.size();
            percentComplete = (int) ((seen / total) * 100);
            setPercentComplete(percentComplete);
            // Repeat the OCR for the present file.
            redoOCR(files.get(i));
            i++;
        }
        if (runStatus != RunStatus.STATUS_TERMINATED) {
            setPercentComplete(100);
        }
        Singleton.getSingletonInstance().getMainFrame().notifyListener(runStatus,
                this);
        report();
        done();
    }

    private void setPercentComplete(int aPercentage) {
        // set value
        percentComplete = aPercentage;
        log.debug("Debug {}", percentComplete);
        // notify listeners
        Singleton.getSingletonInstance().getMainFrame().notifyListener(
                percentComplete, this);
        Iterator<RunnerListener> i = listeners.iterator();
        while (i.hasNext()) {
            i.next().notifyListener(percentComplete, this);
        }
    }

    /**
     * Cleanup when job is complete.
     */
    private void done() {
        Singleton.getSingletonInstance().getJobList().removeJob(this);
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
     */
    @Override
    public boolean stop() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        start();
    }

    private void report() {
        String report = "Results of redone OCR on Image files.\n";
        report += counter.toString();
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                "OCR re-do complete.");
        RunnableJobReportDialog errorReportDialog = new RunnableJobReportDialog(
                Singleton.getSingletonInstance().getMainFrame(), report,
                counter.getErrors(), "Repeat OCR Results");
        errorReportDialog.setVisible(true);
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    @Override
    public String getName() {
        if (this.scan == SCAN_ALL) {
            return "Redo OCR for All specimens in state " + WorkFlowStatus.STAGE_0;
        } else {
            return "Redo OCR for specimens in state " + WorkFlowStatus.STAGE_0;
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */
    @Override
    public Date getStartTime() {
        return startDate;
    }
}
