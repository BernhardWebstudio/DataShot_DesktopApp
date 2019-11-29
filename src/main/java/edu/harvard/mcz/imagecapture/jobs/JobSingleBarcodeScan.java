/**
 * BarcodeScanJob.java
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
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.interfaces.*;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog;
import edu.harvard.mcz.imagecapture.utility.FileUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Examines a single image file attempts to determine the correct template, tries to parse data from the image, launches
 * a display of the barcode, labels, unit tray label, and specimen parts of the image, and if persistence is requested,
 * stores the image and specimen data in the database and launches a UI for editing the specimen record.
 * The image file to scan is selected by the user from a file picker dialog which is launched when starting the job.
 * <p>
 * Usage:
 * <pre>
 * JobSingleBarcodeScan s = new JobSingleBarcodeScan(true);
 * s.start();
 * </pre>
 */
public class JobSingleBarcodeScan extends AbstractFileScanJob {

    private static final Log log = LogFactory.getLog(JobSingleBarcodeScan.class);

    private boolean persist = false;
    private Date startDate = null;
    private int runStatus = RunStatus.STATUS_NEW;

    /**
     * Default constructor, creates a single image job with persist=false, allows examination of image
     * extracted barcode, and OCR of label data without making a database connection.
     */
    public JobSingleBarcodeScan() {
        super();
        init();
    }

    /**
     * Constructor allowing specification of persistence.
     *
     * @param persistResult if true enables connection to database to persist changes, and adds editable form
     *                      showing the current specimen record in the database that matches the barcode extracted from the image, creating
     *                      the image and specimen records if needed.
     */
    public JobSingleBarcodeScan(boolean persistResult) {
        super();
        persist = persistResult;
        init();
    }

    static Specimen setBasicSpecimenFromParser(TaxonNameReturner parser, Specimen s) {
        if (s.getFamily().length() > 40) {
            s.setFamily(s.getFamily().substring(0, 40));
        }

        if (parser != null) {
            s.setGenus(parser.getGenus());
            s.setSpecificEpithet(parser.getSpecificEpithet());
            s.setSubspecificEpithet(parser.getSubspecificEpithet());
            s.setInfraspecificEpithet(parser.getInfraspecificEpithet());
            s.setInfraspecificRank(parser.getInfraspecificRank());
            s.setAuthorship(parser.getAuthorship());
            s.setDrawerNumber(((DrawerNameReturner) parser).getDrawerNumber());
            s.setCollection(((CollectionReturner) parser).getCollection());
        }
        return s;
    }

    private void init() {
        listeners = new ArrayList<RunnerListener>();
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#cancel()
     */
    @Override
    public boolean cancel() {
        runStatus = RunStatus.STATUS_TERMINATED;
        return false;
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#getStatus()
     */
    @Override
    public int getStatus() {
        return runStatus;
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#registerListener(edu.harvard.mcz.imagecapture.RunnerListener)
     */
    @Override
    public boolean registerListener(RunnerListener jobListener) {
        if (listeners == null) {
            init();
        }
        return listeners.add(jobListener);
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#start()
     */
    @Override
    public void start() {
        startDate = new Date();
        Singleton.getSingletonInstance().getJobList().addJob(this);
        setPercentComplete(0);
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Selecting file to check.");
        // ask for the file to parse
        File fileToCheck = FileUtility.askForImageFile(new File(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_LASTPATH)));
        setPercentComplete(10);
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Scanning file for barcode.");
        if (fileToCheck != null) {
            Singleton.getSingletonInstance().getProperties().getProperties().setProperty(ImageCaptureProperties.KEY_LASTPATH, fileToCheck.getPath());
            String filename = fileToCheck.getName();
            log.debug("Selected file " + filename + " to scan for barcodes");
            Counter counter = new Counter();
            checkFile(fileToCheck, counter);
            RunnableJobReportDialog errorReportDialog = new RunnableJobReportDialog(Singleton.getSingletonInstance().getMainFrame(), counter.toString(), counter.getErrors(), "Preprocess Result");
            errorReportDialog.setVisible(true);
        } else {
            log.error("No file selected from dialog.");
        }
        setPercentComplete(100);
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage("");
        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        Singleton.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount());
        Singleton.getSingletonInstance().getJobList().removeJob(this);
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#stop()
     */
    @Override
    public boolean stop() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int percentComplete() {
        return percentComplete;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        start();
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    @Override
    public String getName() {
        return "Scan a single file for barcodes";
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */
    @Override
    public Date getStartTime() {
        return startDate;
    }

}
