/**
 * JobAllImageFilesScan.java
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

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.RunStatus;
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob;
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog;
import edu.harvard.mcz.imagecapture.utility.FileUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

/**
 * Check all image files either under the image root directory or in a selected directory
 * and add records for files that aren't yet known to the database that contain barcode
 * information and add corresponding specimen records for new specimens.
 */
public class JobAllImageFilesScan extends AbstractFileScanJob {

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
    private static final Log log = LogFactory.getLog(JobAllImageFilesScan.class);
    private int scan = SCAN_ALL;     // default scan all
    private File startPointSpecific = null;  // place to start for scan_specific
    private File startPoint = null;  // start point used.
    private int runStatus = RunStatus.STATUS_NEW;
    private int thumbnailCounter = 0;
    private Date startTime = null;

    /**
     * Default constructor, creates a job to scan all of imagebase, unless imagebase is
     * unreadable or undefined, in which case a directory chooser dialog is launched.
     */
    public JobAllImageFilesScan() {
        super();
        scan = SCAN_ALL;
        startPointSpecific = null;
        runStatus = RunStatus.STATUS_NEW;
        init();
    }

    /**
     * Create a scan job to bring up dialog to pick a specific directory to scan, or
     * to scan a specific directory specified by startAt.
     * <p>
     * Behavior:
     * <p>
     * whatToScan=SCAN_ALL, startAt is ignored, equivalent to default constructor.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for scan (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_ALL, SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    public JobAllImageFilesScan(int whatToScan, File startAt) {
        super();
        scan = SCAN_SELECT;
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt;
        }
        if (whatToScan == SCAN_ALL) {
            // equivalent to default constructor
            scan = SCAN_ALL;
            startPointSpecific = null;
        } else if (whatToScan == SCAN_SPECIFIC) {
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

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    @Override
    public boolean cancel() {
        runStatus = RunStatus.STATUS_TERMINATED;
        return false;
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStatus()
     */
    @Override
    public int getStatus() {
        return runStatus;
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#registerListener
     */
    @Override
    public boolean registerListener(RunnerListener jobListener) {
        if (listeners == null) {
            init();
        }
        return listeners.add(jobListener);
    }

    /**
     * Start this job by setting up the directories, making sure all are readable and start pointers are set correctly
     *
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    @Override
    public void start() {
        startTime = new Date();
        Singleton.getSingletonInstance().getJobList().addJob(this);
        runStatus = RunStatus.STATUS_RUNNING;
        startPoint = null;
        // place to start the scan from, imageBase directory for SCAN_ALL
        String imageBasePath = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
        File imageBase = null;
        if (imageBasePath != null) {
            imageBase = new File(imageBasePath);
        }

        // If it isn't null, retrieve the image base directory from properties, and test for read access.
        if (imageBase == null || !imageBase.canRead()) {
            JOptionPane.showMessageDialog(Singleton.getSingletonInstance().getMainFrame(), "Can't start scan. Don't know where images are stored or can't read. Set imagebase property '" + ImageCaptureProperties.KEY_IMAGEBASE + "'. Does the path exist?", "Can't Scan.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        startPoint = imageBase;

        // setup directory scan in case of SCAN_SPECIFIC
        if (scan == SCAN_SPECIFIC && startPointSpecific != null && startPointSpecific.canRead()) {
            // A scan start point has been provided, don't launch a dialog.
            startPoint = startPointSpecific;
        }
        // ask for file directory to scan in case of SCAN_SELECT
        if (scan == SCAN_SELECT) {
            // launch a file chooser dialog to select the directory to scan
            File searchStartPoint = null;
            if (startPointSpecific != null && startPointSpecific.canRead()) {
                searchStartPoint = startPointSpecific;
            } else {
                if (Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_LASTPATH) != null) {
                    searchStartPoint = new File(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_LASTPATH));
                }
            }
            startPoint = FileUtility.askForDirectory(searchStartPoint);
            if (startPoint == null) {
                JOptionPane.showMessageDialog(Singleton.getSingletonInstance().getMainFrame(), "Can't scan without a directory.", "Error: No scanning possible.", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Check that fileToCheck is within imageBase.
        if (!ImageCaptureProperties.isInPathBelowBase(startPoint)) {
            String base = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(
                    ImageCaptureProperties.KEY_IMAGEBASE);
            log.error("Tried to scan directory (" + startPoint.getPath() + ") outside of base image directory (" + base + ")");
            String message = "Should and will not scan and database files outside of base image directory (" + base + ")";
            JOptionPane.showMessageDialog(Singleton.getSingletonInstance().getMainFrame(), message, "Won't scan outside image base directory.", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            if (!startPoint.canRead()) {
                JOptionPane.showMessageDialog(Singleton.getSingletonInstance().getMainFrame(), "Can't start scan.  Unable to read selected directory: " + startPoint.getPath(), "Can't Scan.", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // walk through directory tree
            Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Scanning path: " + startPoint.getPath());
            Counter counter = new Counter();
            // count files to scan
            countFiles(startPoint, counter);
            setPercentComplete(0);
            Singleton.getSingletonInstance().getMainFrame().notifyListener(runStatus, this);
            counter.incrementDirectories();
            // scan
            if (runStatus != RunStatus.STATUS_TERMINATED) {
                checkFiles(startPoint, counter);
            }
            // report

            Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Preprocess scan complete");
            setPercentComplete(100);
            Singleton.getSingletonInstance().getMainFrame().notifyListener(runStatus, this);
            RunnableJobReportDialog errorReportDialog = new RunnableJobReportDialog(Singleton.getSingletonInstance().getMainFrame(), counter.toString(), counter.getErrors(), "Preprocess Results");
            errorReportDialog.setVisible(true);
        }

        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        Singleton.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount());

        done();
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
     */
    @Override
    public boolean stop() {
        runStatus = RunStatus.STATUS_TERMINATED;
        return false;
    }

    @Override
    public int percentComplete() {
        return percentComplete;
    }

    /**
     * Count the files in a directory, recursively
     *
     * @param startPoint the path to start
     * @param counter    the counter to increment
     */
    private void countFiles(File startPoint, Counter counter) {
        // count files to preprocess
        File[] containedFiles = startPoint.listFiles();
        if (containedFiles != null) {
            for (File fileToCheck : containedFiles) {
                if (fileToCheck.isDirectory()) {
                    if (fileToCheck.canRead()) {
                        countFiles(fileToCheck, counter);
                    }
                } else {
                    counter.incrementTotal();
                }
            }
        }
    }

    /**
     * Do the actual processing of each files
     *
     * @param startPoint the directory with all files to handle
     * @param counter    the counter to increment
     */
    private void checkFiles(File startPoint, Counter counter) {
        // pick jpeg files
        // for each file check name against database, if not found, check barcodes, scan and parse text, create records.
        File[] containedFiles = startPoint.listFiles();
        if (containedFiles == null) {
            log.error("Directory " + startPoint.getPath() + " contains 0 entries.");
            return;
        }
        log.debug("Scanning directory: " + startPoint.getPath() + " containing " + containedFiles.length + " files.");
        // create thumbnails in a separate thread
        (new Thread(new ThumbnailBuilderInternal(startPoint))).start();

        for (File containedFile : containedFiles) {
            if (runStatus != RunStatus.STATUS_TERMINATED) {
                Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Scanning: " + containedFile.getName());
                log.debug("Scanning: " + containedFile.getName());
                if (containedFile.isDirectory()) {
                    // recursive read for all files: start anew for directories
                    if (containedFile.canRead()) {
                        // Skip thumbs directories
                        if (!containedFile.getName().equals("thumbs")) {
                            checkFiles(containedFile, counter);
                            counter.incrementDirectories();
                        }
                    } else {
                        counter.appendError(new RunnableJobError(containedFile.getName(), "", "Could not read directory", new UnreadableFileException(), RunnableJobError.TYPE_FILE_READ));
                        counter.incrementDirectoriesFailed();
                    }
                } else {
                    // check JPEG files for barcodes
                    if (!containedFile.getName().matches(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEREGEX))) {
                        log.debug("Skipping file [" + containedFile.getName() + "], doesn't match expected filename pattern " + Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEREGEX));
                    } else {
                        checkFile(containedFile, counter);
                    }
                }
                // report progress
                Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Scanned: " + containedFile.getName());
                Float seen = 0.0f + counter.getFilesSeen();
                Float total = 0.0f + counter.getTotal();
                // thumbPercentComplete = (int) ((seen/total)*100);
                setPercentComplete((int) ((seen / total) * 100));
            }
            Singleton.getSingletonInstance().getMainFrame().notifyListener(runStatus, this);
        }
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    @Override
    public String getName() {
        if (scan == SCAN_ALL) {
            return "Preprocess all image files";
        } else {
            String sp = startPoint.getPath();
            if (sp == null || sp.length() == 0) {
                sp = startPointSpecific.getPath();
            }
            return "Preprocess images in " + sp;
        }
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */
    @Override
    public Date getStartTime() {
        return startTime;
    }

    protected class ThumbnailBuilderInternal implements Runnable, RunnableJob {

        File startPoint;

        String thumbHeight;
        String thumbWidth;

        private Date thumbStartTime = null;
        private int thumbRunStatus = RunStatus.STATUS_NEW;
        private int thumbPercentComplete = 0;

        private ArrayList<RunnerListener> thumbListeners = null;

        public ThumbnailBuilderInternal(File aStartPoint) {
            startPoint = aStartPoint;
            thumbHeight = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_THUMBNAIL_HEIGHT);
            thumbWidth = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_THUMBNAIL_WIDTH);
            thumbInit();
        }

        public ThumbnailBuilderInternal(File aStartPoint, int thumbHeightPixels, int thumbWidthPixels) {
            startPoint = aStartPoint;
            thumbHeight = Integer.toString(thumbHeightPixels);
            thumbWidth = Integer.toString(thumbWidthPixels);
            if (thumbHeightPixels < 10) {
                thumbHeight = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_THUMBNAIL_HEIGHT);
            }
            if (thumbWidthPixels < 10) {
                thumbWidth = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_THUMBNAIL_WIDTH);
            }
            thumbInit();
        }

        protected void thumbInit() {
            thumbListeners = new ArrayList<RunnerListener>();
        }

        @Override
        public void run() {
            thumbStartTime = new Date();
            thumbRunStatus = RunStatus.STATUS_RUNNING;
            setThumbPercentComplete(0);
            Singleton.getSingletonInstance().getJobList().addJob(this);
            // mkdir thumbs ; mogrify -path thumbs -resize 80x120 *.JPG
            if (startPoint.isDirectory() && (!startPoint.getName().equals("thumbs"))) {
                File thumbsDir = new File(startPoint.getPath() + File.separator + "thumbs");
                log.debug(thumbsDir.getPath());
                if (!thumbsDir.exists()) {
                    thumbsDir.mkdir();
                    Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Creating " + thumbsDir.getPath());
                }
                // Runtime executes mogrify directly, not through a shell, thus expand list of files to pass
                // rather than passing *.JPG
                File[] potentialFilesToThumb = startPoint.listFiles();
                StringBuffer filesToThumb = new StringBuffer();
                int filesToThumbCount = 0;
                for (int i = 0; i < potentialFilesToThumb.length; i++) {
                    if (potentialFilesToThumb[i].getName().endsWith(".JPG")) {
                        filesToThumb.append(potentialFilesToThumb[i].getName()).append(" ");
                        filesToThumbCount++;
                    }
                }
                if (filesToThumbCount > 0) {
                    boolean makeWithJava = false;

                    String mogrify = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_MOGRIFY_EXECUTABLE);
                    if (mogrify == null || mogrify.trim().length() > 0) {
                        makeWithJava = true;
                    } else {
                        String runCommand = mogrify + " -path thumbs -resize " + thumbWidth + "x" + thumbHeight + " " + filesToThumb.toString();


                        Runtime r = Runtime.getRuntime();
                        log.debug(runCommand);
                        try {
                            String[] env = {""};
                            Process proc = r.exec(runCommand, env, startPoint);
                            InputStream stderr = proc.getErrorStream();
                            InputStreamReader isrstderr = new InputStreamReader(stderr);
                            BufferedReader br = new BufferedReader(isrstderr);
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                log.debug("stderr:" + line);
                            }
                            int exitVal = proc.waitFor();
                            log.debug("Mogrify Process exitValue: " + exitVal);
                            if (exitVal == 0) {
                                thumbnailCounter++;
                                String message = "Finished creating thumbnails in: " + startPoint.getPath();
                                Singleton.getSingletonInstance().getMainFrame().setStatusMessage(message);
                                log.debug(message);
                            } else {
                                log.error("Error returned running " + runCommand);
                                makeWithJava = true;
                            }
                        } catch (IOException e) {
                            log.error("Error running: " + runCommand);
                            e.printStackTrace();
                            Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Error creating thumbnails " + e.getMessage());
                            makeWithJava = true;
                        } catch (InterruptedException e) {
                            log.error("Mogrify process interupted");
                            e.printStackTrace();
                        }

                    }

                    if (makeWithJava) {
                        for (int i = 0; i < potentialFilesToThumb.length; i++) {
                            try {
                                log.debug("Attempting thumbnail generation with java in " + startPoint.getPath());
                                log.debug("Attempting thumbnail generation with java to " + thumbsDir.getPath());
                                ArrayList<String> makeFrom = new ArrayList<String>();
                                List<File> files = Arrays.asList(startPoint.listFiles());
                                Iterator<File> it = files.iterator();
                                int creationCounter = 0;
                                int totalFiles = files.size();
                                while (it.hasNext() && thumbRunStatus != RunStatus.STATUS_CANCEL_REQUESTED) {
                                    File file = it.next();
                                    if (!file.isDirectory() && file.exists() && file.canRead()) {
                                        // file must exist and be readable to make thumbnail
                                        if (file.getName().matches(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEREGEX))) {
                                            // only try to make thumbnails of files that match the image file pattern.
                                            makeFrom.add(file.getPath());
                                            log.debug(file.getPath());
                                            File target = new File(thumbsDir.getPath() + File.separatorChar + file.getName());
                                            log.debug(target.getPath());
                                            if (!target.exists()) {
                                                BufferedImage img = new BufferedImage(Integer.parseInt(thumbWidth), Integer.parseInt(thumbHeight), BufferedImage.TYPE_INT_RGB);
                                                img.createGraphics().drawImage(ImageIO.read(file).getScaledInstance(80, 120, Image.SCALE_SMOOTH), 0, 0, null);
                                                ImageIO.write(img, "jpg", target);
                                                creationCounter++;
                                            }
                                        }
                                        setThumbPercentComplete((int) (((float) creationCounter / totalFiles) * 100));
                                    }
                                }
                                String message = "Finished creating thumbnails (" + creationCounter + ") in: " + startPoint.getPath();
                                Singleton.getSingletonInstance().getMainFrame().setStatusMessage(message);
                            } catch (IOException e) {
                                log.error("Thumbnail generation with thumbnailator library failed");
                                log.error(e.getMessage());
                            }
                        }
                    }

                } else {
                    String message = "No *.JPG files found in " + startPoint.getPath();
                    Singleton.getSingletonInstance().getMainFrame().setStatusMessage(message);
                    log.debug(message);
                }
            }
            String message = "Thumbnail Generation Complete.";
            Singleton.getSingletonInstance().getMainFrame().setStatusMessage(message);
            Singleton.getSingletonInstance().getJobList().removeJob(this);
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
         */
        @Override
        public void start() {
            run();
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
         */
        @Override
        public boolean stop() {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
         */
        @Override
        public boolean cancel() {
            thumbRunStatus = RunStatus.STATUS_CANCEL_REQUESTED;
            return false;
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStatus()
         */
        @Override
        public int getStatus() {
            return thumbRunStatus;
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#percentComplete()
         */
        @Override
        public int percentComplete() {
            return thumbPercentComplete;
        }

        protected void setThumbPercentComplete(int aPercentage) {
            //set value
            thumbPercentComplete = aPercentage;
            log.debug(thumbPercentComplete);
            //notify listeners
            Iterator<RunnerListener> i = thumbListeners.iterator();
            while (i.hasNext()) {
                i.next().notifyListener(thumbPercentComplete, this);
            }
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#registerListener(edu.harvard.mcz.imagecapture.interfaces.RunnerListener)
         */
        @Override
        public boolean registerListener(RunnerListener aJobListener) {
            if (thumbListeners == null) {
                thumbInit();
            }
            return thumbListeners.add(aJobListener);
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
         */
        @Override
        public String getName() {
            return "Thumbnail Generation in: " + startPoint;
        }

        /**
         * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
         */
        @Override
        public Date getStartTime() {
            return thumbStartTime;
        }

    }  // end class ThumbnailBuilder

}

