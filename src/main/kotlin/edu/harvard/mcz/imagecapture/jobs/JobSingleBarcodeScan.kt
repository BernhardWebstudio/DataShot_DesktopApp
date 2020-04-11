/**
 * BarcodeScanJob.java
 * edu.harvard.mcz.imagecapture
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 */
package edu.harvard.mcz.imagecapture.jobs


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.RunStatus
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScan
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import edu.harvard.mcz.imagecapture.utility.FileUtility
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Examines a single image file attempts to determine the correct template, tries to parse data from the image, launches
 * a display of the barcode, labels, unit tray label, and specimen parts of the image, and if persistence is requested,
 * stores the image and specimen data in the database and launches a UI for editing the specimen record.
 * The image file to scan is selected by the user from a file picker dialog which is launched when starting the job.
 *
 *
 * Usage:
 * <pre>
 * JobSingleBarcodeScan s = new JobSingleBarcodeScan(true);
 * s.start();
</pre> *
 */
class JobSingleBarcodeScan : AbstractFileScanJob {
    protected var percentComplete = 0
    protected var listeners: ArrayList<RunnerListener?> = ArrayList<RunnerListener?>()
    private var persist = false
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */ var startTime: Date? = null
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#getStatus()
     */ var status: Int = RunStatus.STATUS_NEW
        private set

    /**
     * Default constructor, creates a single image job with persist=false, allows examination of image
     * extracted barcode, and OCR of label data without making a database connection.
     */
    constructor() : super() {
        init()
    }

    /**
     * Constructor allowing specification of persistence.
     *
     * @param persistResult if true enables connection to database to persist changes, and adds editable form
     * showing the current specimen record in the database that matches the barcode extracted from the image, creating
     * the image and specimen records if needed.
     */
    constructor(persistResult: Boolean) : super() {
        persist = persistResult
        init()
    }

    private fun init() {
        listeners = ArrayList<RunnerListener?>()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#cancel()
     */
    fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        return false
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#registerListener(edu.harvard.mcz.imagecapture.RunnerListener)
     */
    fun registerListener(jobListener: RunnerListener?): Boolean {
        if (listeners == null) {
            init()
        }
        return listeners.add(jobListener)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#start()
     */
    fun start() {
        startTime = Date()
        Singleton.getJobList().addJob(this)
        setPercentComplete(0)
        Singleton.getMainFrame().setStatusMessage("Selecting file to check.")
        // ask for the file to parse
        val fileToCheck: File = FileUtility.askForImageFile(File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH)))
        setPercentComplete(10)
        Singleton.getMainFrame().setStatusMessage("Scanning file for barcode.")
        if (fileToCheck != null) {
            Singleton.getProperties().getProperties().setProperty(ImageCaptureProperties.Companion.KEY_LASTPATH, fileToCheck.path)
            val filename = fileToCheck.name
            log!!.debug("Selected file $filename to scan for barcodes")
            val counter = Counter()
            val locks: Array<Lock?> = arrayOfNulls<ReentrantLock?>(1)
            checkFile(fileToCheck, counter, locks)
            val errorReportDialog = RunnableJobReportDialog(Singleton.getMainFrame(), counter.toString(), counter.errors, "Preprocess Result")
            errorReportDialog.setVisible(true)
        } else {
            log!!.error("No file selected from dialog.")
        }
        setPercentComplete(100)
        Singleton.getMainFrame().setStatusMessage("")
        val sls = SpecimenLifeCycle()
        Singleton.getMainFrame().setCount(sls.findSpecimenCount())
        Singleton.getJobList().removeJob(this)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.Runnable#stop()
     */
    fun stop(): Boolean { // TODO Auto-generated method stub
        return false
    }

    fun percentComplete(): Int {
        return percentComplete
    }

    /**
     * Set the completeness percentage in main frame & notify listeners
     *
     * @param aPercentage the progress percentage
     */
    protected fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        //notify listeners
        Singleton.getMainFrame().notifyListener(percentComplete, this)
        for (listener in listeners) {
            listener.notifyListener(percentComplete, this)
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    override fun run() {
        start()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = "Scan a single file for barcodes"

    companion object {
        private val log = LogFactory.getLog(JobSingleBarcodeScan::class.java)
    }
}
