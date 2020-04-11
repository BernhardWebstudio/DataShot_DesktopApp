/**
 * JobAllImageFilesScan.java
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
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScan
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import edu.harvard.mcz.imagecapture.utility.FileUtility
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.swing.JOptionPane

/**
 * Check all image files either under the image root directory or in a selected directory
 * and add records for files that aren't yet known to the database that contain barcode
 * information and add corresponding specimen records for new specimens.
 */
class JobAllImageFilesScan : AbstractFileScanJob {
    protected var percentComplete = 0
    protected var listeners: ArrayList<RunnerListener?> = ArrayList<RunnerListener?>()
    private var scan = SCAN_ALL // default scan all
    private var startPointSpecific: File? = null // place to start for scan_specific
    private var startPoint: File? = null // start point used.
    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.getStatus
     */
    var status: Int = RunStatus.STATUS_NEW
        private set
    private var thumbnailCounter: AtomicInteger? = null
    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.getStartTime
     */
    var startTime: Date? = null
        private set

    /**
     * Default constructor, creates a job to scan all of imagebase, unless imagebase is
     * unreadable or undefined, in which case a directory chooser dialog is launched.
     */
    constructor() : super() {
        startPointSpecific = null
        thumbnailCounter = AtomicInteger(0)
        init()
    }

    /**
     * Create a scan job to bring up dialog to pick a specific directory to scan, or
     * to scan a specific directory specified by startAt.
     *
     *
     * Behavior:
     *
     *
     * whatToScan=SCAN_ALL, startAt is ignored, equivalent to default constructor.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for scan (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_ALL, SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    constructor(whatToScan: Int, startAt: File?) : super() {
        scan = SCAN_SELECT
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt
        }
        if (whatToScan == SCAN_ALL) { // equivalent to default constructor
            scan = SCAN_ALL
            startPointSpecific = null
        } else if (whatToScan == SCAN_SPECIFIC) {
            scan = if (startAt != null && startAt.canRead()) {
                SCAN_SPECIFIC
            } else {
                SCAN_SELECT
            }
        }
        status = RunStatus.STATUS_NEW
        init()
    }

    protected fun init() {
        listeners = ArrayList<RunnerListener?>()
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.cancel
     */
    fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        return false
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.registerListener
     */
    fun registerListener(jobListener: RunnerListener?): Boolean {
        if (listeners == null) {
            init()
        }
        return listeners.add(jobListener)
    }

    /**
     * Start this job by setting up the directories, making sure all are readable and start pointers are set correctly
     *
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.start
     */
    fun start() {
        startTime = Date()
        Singleton.JobList.addJob(this)
        status = RunStatus.STATUS_RUNNING
        startPoint = null
        // place to start the scan from, imageBase directory for SCAN_ALL
        val imageBasePath: String = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE)
        var imageBase: File? = null
        if (imageBasePath != null) {
            imageBase = File(imageBasePath)
        }
        // If it isn't null, retrieve the image base directory from properties, and test for read access.
        if (imageBase == null || !imageBase.canRead()) {
            JOptionPane.showMessageDialog(Singleton.MainFrame, "Can't start scan. Don't know where images are stored or can't read. Set imagebase property '" + ImageCaptureProperties.Companion.KEY_IMAGEBASE + "'. Does the path exist?", "Can't Scan.", JOptionPane.ERROR_MESSAGE)
            return
        }
        startPoint = imageBase
        // setup directory scan in case of SCAN_SPECIFIC
        if (scan == SCAN_SPECIFIC && startPointSpecific != null && startPointSpecific!!.canRead()) { // A scan start point has been provided, don't launch a dialog.
            startPoint = startPointSpecific
        }
        // ask for file directory to scan in case of SCAN_SELECT
        if (scan == SCAN_SELECT) { // launch a file chooser dialog to select the directory to scan
            var searchStartPoint: File? = null
            if (startPointSpecific != null && startPointSpecific!!.canRead()) {
                searchStartPoint = startPointSpecific
            } else {
                if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH) != null) {
                    searchStartPoint = File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH))
                }
            }
            startPoint = FileUtility.askForDirectory(searchStartPoint)
            if (startPoint == null) {
                JOptionPane.showMessageDialog(Singleton.MainFrame, "Can't scan without a directory.", "Error: No scanning possible.", JOptionPane.ERROR_MESSAGE)
                return
            }
        }
        // Check that fileToCheck is within imageBase.
        if (!ImageCaptureProperties.Companion.isInPathBelowBase(startPoint)) {
            val base: String = Singleton.Properties.Properties.getProperty(
                    ImageCaptureProperties.Companion.KEY_IMAGEBASE)
            log!!.error("Tried to scan directory (" + startPoint!!.path + ") outside of base image directory (" + base + ")")
            val message = "Should and will not scan and database files outside of base image directory ($base)"
            JOptionPane.showMessageDialog(Singleton.MainFrame, message, "Won't scan outside image base directory.", JOptionPane.ERROR_MESSAGE)
            return
        } else {
            if (!startPoint!!.canRead()) {
                JOptionPane.showMessageDialog(Singleton.MainFrame, "Can't start scan.  Unable to read selected directory: " + startPoint!!.path, "Can't Scan.", JOptionPane.ERROR_MESSAGE)
                return
            }
            // walk through directory tree
            Singleton.MainFrame.setStatusMessage("Scanning path: " + startPoint!!.path)
            val counter = Counter()
            // count files to scan
            countFiles(startPoint!!, counter)
            setPercentComplete(0)
            Singleton.MainFrame.notifyListener(status, this)
            counter.incrementDirectories()
            // scan
            if (status != RunStatus.STATUS_TERMINATED) {
                checkFiles(startPoint!!, counter)
            }
            // report
            Singleton.MainFrame.setStatusMessage("Preprocess scan complete")
            setPercentComplete(100)
            Singleton.MainFrame.notifyListener(status, this)
            val errorReportDialog = RunnableJobReportDialog(Singleton.MainFrame, counter.toString(), counter.errors, "Preprocess Results")
            errorReportDialog.setVisible(true)
        }
        val sls = SpecimenLifeCycle()
        Singleton.MainFrame.setCount(sls.findSpecimenCount())
        done()
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.stop
     */
    fun stop(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        return false
    }

    fun percentComplete(): Int {
        return percentComplete
    }

    /**
     * Count the files in a directory, recursively
     *
     * @param startPoint the path to start
     * @param counter    the counter to increment
     */
    private fun countFiles(startPoint: File, counter: Counter) { // count files to preprocess
        val containedFiles = startPoint.listFiles()
        if (containedFiles != null) {
            for (fileToCheck in containedFiles) {
                if (fileToCheck.isDirectory) {
                    if (fileToCheck.canRead()) {
                        countFiles(fileToCheck, counter)
                    }
                } else {
                    counter.incrementTotal()
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
    private fun checkFiles(startPoint: File, counter: Counter) { // pick jpeg files
// for each file check name against database, if not found, check barcodes, scan and parse text, create records.
        val containedFiles = startPoint.listFiles()
        if (containedFiles == null) {
            log!!.error("Directory " + startPoint.path + " contains 0 entries.")
            return
        }
        log!!.debug("Scanning directory: " + startPoint.path + " containing " + containedFiles.size + " files.")
        val localCounter = AtomicCounter()
        // limit number of parallel threads because we can. And to help Hibernate from dying
        val concurrencyLevel: Int = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_CONCURRENCY_LEVEL, "16").toInt()
        val es: ExecutorService = Executors.newFixedThreadPool(concurrencyLevel)
        val locks: Array<Lock?> = arrayOfNulls<ReentrantLock?>(concurrencyLevel)
        for (i in locks.indices) {
            locks[i] = ReentrantLock()
        }
        // create thumbnails in a separate thread (if requested)
        if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_GENERATE_THUMBNAILS) == "true") {
            es.execute(ThumbnailBuilderJob(startPoint, thumbnailCounter))
        }
        // loop files
        for (containedFile in containedFiles) {
            if (status != RunStatus.STATUS_TERMINATED) {
                Singleton.MainFrame.setStatusMessage("Scanning: " + containedFile.name)
                log.debug("Scanning: " + containedFile.name)
                if (containedFile.isDirectory) { // recursive read for all files: start anew for directories
                    if (containedFile.canRead()) { // Skip thumbs directories
                        if (containedFile.name != "thumbs") {
                            checkFiles(containedFile, counter)
                            counter.incrementDirectories()
                        }
                    } else {
                        counter.appendError(RunnableJobError(containedFile.name, "", "Could not read directory", UnreadableFileException(), RunnableJobError.Companion.TYPE_FILE_READ))
                        counter.incrementDirectoriesFailed()
                    }
                } else { // check JPEG files for barcodes
                    if (!containedFile.name.matches(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEREGEX))) {
                        log.debug("Skipping file [" + containedFile.name + "], doesn't match expected filename pattern " + Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEREGEX))
                    } else { // images belonging to the same specimen creating the same specimen resp. looking for it
// that's why we have here a rather ugly lock handling mechanism
                        es.execute(Runnable { checkFile(containedFile, localCounter, locks) })
                    }
                }
                // report progress
                Singleton.MainFrame.setStatusMessage("Scanned: " + containedFile.name)
                val seen: Float = 0.0f + localCounter.FilesSeen
                val total = 0.0f + counter.total
                // thumbPercentComplete = (int) ((seen/total)*100);
                setPercentComplete((seen / total * 100).toInt())
                log.debug("Setting percent complete: " + seen / total * 100 + " based on seen: " + seen + ", total: " + total)
            }
            Singleton.MainFrame.notifyListener(status, this)
        }
        // assemble all threads
        es.shutdown() // Disable new tasks from being submitted
        try {
            if (!es.awaitTermination(60, TimeUnit.MINUTES)) {
                es.shutdownNow() // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!es.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Execution pool did not terminate")
                }
            }
        } catch (e: InterruptedException) {
            log.error("Execution pool did not terminate", e)
            es.shutdownNow()
            Thread.currentThread().interrupt()
        }
        counter.mergeIn(localCounter)
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.getName
     */
    val name: String
        get() = if (scan == SCAN_ALL) {
            "Preprocess all image files"
        } else {
            var sp = startPoint!!.path
            if (sp == null || sp.length == 0) {
                sp = startPointSpecific!!.path
            }
            "Preprocess images in $sp"
        }

    /**
     * Set the completeness percentage in main frame & notify listeners
     *
     * @param aPercentage the progress percentage
     */
    protected fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        //notify listeners
        Singleton.MainFrame.notifyListener(percentComplete, this)
        for (listener in listeners) {
            listener.notifyListener(percentComplete, this)
        }
    }

    companion object {
        /**
         * Scan all of image base directory tree.
         */
        const val SCAN_ALL = 0
        /**
         * Open a dialog and scan a specific directory.
         */
        const val SCAN_SELECT = 1
        /**
         * From a file, scan a specific directory.
         */
        const val SCAN_SPECIFIC = 2
        private val log = LogFactory.getLog(JobAllImageFilesScan::class.java)
    }
}
