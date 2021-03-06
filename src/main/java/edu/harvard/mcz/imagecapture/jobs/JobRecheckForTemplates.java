/**
 * JobRecheckForTemplates.java
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

import edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetector;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.RunStatus;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob;
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener;
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * JobRecheckForTemplates, recheck image files that have no template identified,
 * but should have one, and try to identify their template.  Can be run after a
 * new template has been created.
 */
public class JobRecheckForTemplates implements RunnableJob, Runnable {

    /**
     * Recheck all files with no template.
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
    private static final Logger log =
            LoggerFactory.getLogger(JobRecheckForTemplates.class);
    Counter counter = null;         // For reporting results
    private int scan = SCAN_SELECT; // default scan a user selected directory
    private File startPointSpecific = null; // place to start for scan_specific
    private int runStatus = RunStatus.STATUS_NEW;
    private Date startDate = null;
    private int percentComplete = 0;

    private ArrayList<RunnerListener> listeners = null;

    /**
     * Default constructor, scan all
     */
    public JobRecheckForTemplates() {
        init(SCAN_ALL, null);
    }

    /**
     * Create a recheck for templates job to bring up dialog to pick a specific
     * directory on which to recheck image records for templates. <p> Behavior:
     * <p>
     * whatToScan=SCAN_ALL, all records having no template and a linked specimen
     * are rechecked. whatToScan=SCAN_SELECT, startAt is used as starting point
     * for directory chooser dialog. whatToScan=SCAN_SPECIFIC, startAt is used as
     * starting point for repeat (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    public JobRecheckForTemplates(int whatToScan, File startAt) {
        init(whatToScan, startAt);
    }

    public void init(int whatToScan, File startAt) {
        listeners = new ArrayList<RunnerListener>();
        scan = SCAN_SELECT;
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan
        // if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt;
        }
        if (whatToScan == SCAN_ALL) {
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
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    @Override
    public boolean cancel() {
        runStatus = RunStatus.STATUS_TERMINATED;
        log.debug("JobCleanDirectory " + this +
                "  recieved cancel signal");
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
        return listeners.add(jobListener);
    }

    private List<ICImage> getFileList() {
        List<ICImage> files = new ArrayList<ICImage>();
        if (scan != SCAN_ALL) {
            String pathToCheck = "";
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
                    if (!startPoint.canRead()) {
                        JOptionPane.showMessageDialog(
                                Singleton.getSingletonInstance().getMainFrame(),
                                "Can't start scan.  Unable to read selected directory: " +
                                        startPoint.getPath(),
                                "Can't Scan.", JOptionPane.YES_NO_OPTION);
                    } else {
                        pathToCheck = ImageCaptureProperties.getPathBelowBase(startPoint);

                        // retrieve a list of image records in the selected directory
                        ICImageLifeCycle ils = new ICImageLifeCycle();
                        ICImage pattern = new ICImage();
                        pattern.setPath(pathToCheck);
                        pattern.setTemplateId(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS);
                        files = ils.findByExample(pattern);
                        if (files != null) {
                            log.debug("Debug {}", files.size());
                        }
                    }
                }
            }
        } else {
            try {
                // retrieve a list of all image records with no template
                ICImageLifeCycle ils = new ICImageLifeCycle();
                files = ICImageLifeCycle.findNotDrawerNoTemplateImages();
                if (files != null) {
                    log.debug("Debug {}", files.size());
                }
            } catch (HibernateException e) {
                log.error(e.getMessage());
                runStatus = RunStatus.STATUS_FAILED;
                String message =
                        "Error loading the list of images with no templates. " +
                                e.getMessage();
                JOptionPane.showMessageDialog(
                        Singleton.getSingletonInstance().getMainFrame(), message,
                        "Error loading image records.", JOptionPane.YES_NO_OPTION);
            }
        }

        log.debug("Found " + files.size() +
                " Image files without templates in directory to check.");

        return files;
    }

    private void recheckForTemplates(ICImage image) {
        if (image.getSpecimen() != null) {
            File imageFile = new File(ImageCaptureProperties.assemblePathWithBase(
                    image.getPath(), image.getFilename()));
            counter.incrementFilesSeen();

            ConfiguredBarcodePositionTemplateDetector detector =
                    new ConfiguredBarcodePositionTemplateDetector();
            try {
                ICImageLifeCycle ils = new ICImageLifeCycle();
                String templateName = detector.detectTemplateForImage(imageFile);
                if (templateName != null &&
                        !templateName.equals(
                                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
                    // update the image record with this template.
                    image.setTemplateId(templateName);
                    ils.attachDirty(image);
                    counter.incrementFilesUpdated();
                } else if (templateName != null &&
                        templateName.equals(
                                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
                    RunnableJobError error = new RunnableJobError(
                            image.getFilename(), image.getRawBarcode(), "",
                            image.getRawExifBarcode(), "No Template Found.", null, null, null,
                            RunnableJobError.TYPE_NO_TEMPLATE);
                    counter.appendError(error);
                }
            } catch (UnreadableFileException e) {
                log.error(e.getMessage());
                RunnableJobError error = new RunnableJobError(
                        image.getFilename(), image.getRawBarcode(), "",
                        image.getRawExifBarcode(),
                        "Unreadable File Exception checking for template.", null, null,
                        null, RunnableJobError.TYPE_NO_TEMPLATE);
                counter.appendError(error);
            } catch (SaveFailedException e) {
                log.error(e.getMessage(), e);
                RunnableJobError error = new RunnableJobError(
                        image.getFilename(), image.getRawBarcode(), "",
                        image.getRawExifBarcode(),
                        "Save Failed Exception saving new template.", null, null, null,
                        RunnableJobError.TYPE_SAVE_FAILED);
                counter.appendError(error);
            }

        } else {
            log.debug(image.getPath() + image.getFilename() +
                    " Has no attached image.");
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
        // Obtain a list of image file records for the selected directory.
        List<ICImage> files = getFileList();
        log.debug("ReckeckForTemplatesJob started" + this);
        int i = 0;
        while (i < files.size() && runStatus != RunStatus.STATUS_TERMINATED &&
                runStatus != RunStatus.STATUS_FAILED) {
            // Find out how far along the process is
            Float seen = 0.0f + i;
            Float total = 0.0f + files.size();
            percentComplete = (int) ((seen / total) * 100);
            setPercentComplete(percentComplete);
            // Repeat the OCR for the present file.
            recheckForTemplates(files.get(i));
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
        String report =
                "Results of template check on Image files missing templates (WholeImageOnly).\n";
        report += "Found  " + counter.getFilesSeen() +
                " image file database records without templates.\n";
        report += "Updated " + counter.getFilesUpdated() +
                " image records to a template.\n";
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                "Check for templates complete.");
        RunnableJobReportDialog errorReportDialog = new RunnableJobReportDialog(
                Singleton.getSingletonInstance().getMainFrame(), report,
                counter.getErrors(), "Recheck Files for Templates Results");
        errorReportDialog.setVisible(true);
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    @Override
    public String getName() {
        return "Recheck for Templates for images that are " +
                PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS;
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */
    @Override
    public Date getStartTime() {
        return startDate;
    }
}
