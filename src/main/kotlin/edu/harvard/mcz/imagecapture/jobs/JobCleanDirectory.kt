/**
 * JobCleanDirectory.java
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
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.JobCleanDirectory
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane

/**
 * JobCleanDirectory, scan a directory for image files that have been deleted, and remove the related image records.
 */
class JobCleanDirectory : RunnableJob, Runnable {
    var counter: Counter? = null // For reporting results
    var listeners: ArrayList<RunnerListener?>? = null
    private var scan = SCAN_SELECT // default scan a user selected directory
    private var startPointSpecific: File? = null // place to start for scan_specific
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStatus()
     */ var status: Int = RunStatus.STATUS_NEW
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */ var startTime: Date? = null public get() {
        return field
    }
        private set
    private var percentComplete = 0

    /**
     * Default constructor, launch a dialog.  JobCleanDirectory only
     * works on a directory within the mount path, and must find the
     * target directory (as readable) before running, otherwise image
     * records for files that do exist could be removed when the
     * file system is not mounted (at the configured location).
     */
    constructor() {
        init(SCAN_SELECT, null)
    }

    /**
     * Create a clean images job to bring up dialog to pick a specific directory
     * on which to clean up image records.
     *
     *
     * Behavior:
     *
     *
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for repeat (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    constructor(whatToScan: Int, startAt: File?) {
        init(whatToScan, startAt)
    }

    /**
     * Setup initial parameters before run.
     *
     * @param whatToScan one of SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    fun init(whatToScan: Int, startAt: File?) {
        listeners = ArrayList<RunnerListener?>()
        scan = SCAN_SELECT
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt
        }
        if (whatToScan == SCAN_SPECIFIC) {
            scan = if (startAt != null && startAt.canRead()) {
                SCAN_SPECIFIC
            } else {
                SCAN_SELECT
            }
        }
        status = RunStatus.STATUS_NEW
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        log!!.debug("JobCleanDirectory $this  recieved cancel signal")
        return false
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#percentComplete()
     */
    override fun percentComplete(): Int {
        return percentComplete
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#registerListener(edu.harvard.mcz.imagecapture.interfaces.RunnerListener)
     */
    override fun registerListener(jobListener: RunnerListener?): Boolean {
        return listeners!!.add(jobListener)
    }// retrieve a list of image records in the selected directory//TODO: handle error condition
    // Check that startPoint is or is within imagebase.
// launch a file chooser dialog to select the directory to scan// A scan start point has been provided, don't launch a dialog.// If it can't be read, null out imagebase// place to start the scan from, imagebase directory for SCAN_ALL
    // If it isn't null, retrieve the image base directory from properties, and test for read access.

    // Find the path in which to include files.
    private val fileList: MutableList<File?>
        private get() {
            val files = ArrayList<File?>()
            var pathToCheck = ""
            // Find the path in which to include files.
            var imagebase: File? = null // place to start the scan from, imagebase directory for SCAN_ALL
            var startPoint: File? = null
            // If it isn't null, retrieve the image base directory from properties, and test for read access.
            if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE) == null) {
                JOptionPane.showMessageDialog(Singleton.MainFrame, "Can't start scan.  Don't know where images are stored.  Set imagbase property.", "Can't Scan.", JOptionPane.ERROR_MESSAGE)
            } else {
                imagebase = File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                if (imagebase != null) {
                    if (imagebase.canRead()) {
                        startPoint = imagebase
                    } else { // If it can't be read, null out imagebase
                        imagebase = null
                    }
                }
                if (scan == SCAN_SPECIFIC && startPointSpecific != null && startPointSpecific!!.canRead()) { // A scan start point has been provided, don't launch a dialog.
                    startPoint = startPointSpecific
                }
                if (imagebase == null || scan == SCAN_SELECT) { // launch a file chooser dialog to select the directory to scan
                    val fileChooser = JFileChooser()
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
                    if (scan == SCAN_SELECT && startPointSpecific != null && startPointSpecific!!.canRead()) {
                        fileChooser.setCurrentDirectory(startPointSpecific)
                    } else {
                        if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH) != null) {
                            fileChooser.setCurrentDirectory(File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH)))
                        }
                    }
                    val returnValue: Int = fileChooser.showOpenDialog(Singleton.MainFrame)
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        val file: File = fileChooser.SelectedFile
                        log!!.debug("Selected base directory: " + file.name + ".")
                        startPoint = file
                    } else { //TODO: handle error condition
                        log!!.error("Directory selection cancelled by user.")
                    }
                }
                // Check that startPoint is or is within imagebase.
                if (!ImageCaptureProperties.Companion.isInPathBelowBase(startPoint)) {
                    val base: String = Singleton.Properties.Properties.getProperty(
                            ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                    log!!.error("Tried to scan directory (" + startPoint!!.path + ") outside of base image directory (" + base + ")")
                    val message = "Can't scan and cleanup files outside of base image directory ($base)"
                    JOptionPane.showMessageDialog(Singleton.MainFrame, message, "Can't Scan outside image base directory.", JOptionPane.YES_NO_OPTION)
                } else if (ImageCaptureProperties.Companion.getPathBelowBase(startPoint).trim({ it <= ' ' }).length == 0) {
                    val base: String = Singleton.Properties.Properties.getProperty(
                            ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                    log!!.error("Tried to scan directory (" + startPoint!!.path + ") which is the base image directory.")
                    val message = "Can only scan and cleanup files in a selected directory within the base directory  ($base).\nYou must select some subdirectory within the base directory to cleanup."
                    JOptionPane.showMessageDialog(Singleton.MainFrame, message, "Can't Cleanup image base directory.", JOptionPane.YES_NO_OPTION)
                } else {
                    if (!startPoint!!.canRead()) {
                        JOptionPane.showMessageDialog(Singleton.MainFrame, "Can't start scan.  Unable to read selected directory: " + startPoint.path, "Can't Scan.", JOptionPane.YES_NO_OPTION)
                    } else {
                        pathToCheck = ImageCaptureProperties.Companion.getPathBelowBase(startPoint)
                        // retrieve a list of image records in the selected directory
                        val ils = ICImageLifeCycle()
                        val images: MutableList<ICImage?> = ils.findAllInDir(pathToCheck)
                        val iter: MutableIterator<ICImage?> = images.iterator()
                        while (iter.hasNext()) {
                            val image: ICImage? = iter.next()
                            val imageFile = File(assemblePathWithBase(image.Path, image.Filename))
                            files.add(imageFile)
                            counter!!.incrementFilesSeen()
                        }
                    }
                }
            }
            log!!.debug("Found " + files.size + " Image files in directory to check.")
            return files
        }

    private fun cleanupFile(file: File) {
        log!!.debug(file)
        val filename = file.name
        if (file.exists()) {
            log.debug(file)
            counter!!.incrementFilesExisting()
        } else {
            val targetPath: String = ImageCaptureProperties.Companion.getPathBelowBase(file.parentFile)
            val ils = ICImageLifeCycle()
            val pattern = ICImage()
            pattern.setPath(targetPath)
            pattern.setFilename(file.name)
            log.debug(targetPath)
            log.debug(file.name)
            val images: MutableList<ICImage?> = ils.findByExample(pattern)
            val iter: MutableIterator<ICImage?> = images.iterator()
            log.debug(images.size)
            while (iter.hasNext()) {
                val image: ICImage? = iter.next()
                log.debug(image.Path)
                log.debug(image.Filename)
                try {
                    val error = RunnableJobError(image.Path, image.RawBarcode, "Deleted while cleaning up.", Exception(), RunnableJobError.Companion.TYPE_UNKNOWN)
                    ils.delete(image)
                    counter!!.appendError(error)
                    counter!!.incrementFilesFailed()
                } catch (e: SaveFailedException) {
                    log.error(e.message, e)
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        Singleton.JobList.addJob(this)
        counter = Counter()
        // Obtain a list of image file records for the selected directory.
        val files = fileList
        log!!.debug("CleanDirectoryJob started$this")
        var i = 0
        while (i < files.size && status != RunStatus.STATUS_TERMINATED) { // Find out how far along the process is
            val seen = 0.0f + i
            val total = 0.0f + files.size
            percentComplete = (seen / total * 100).toInt()
            setPercentComplete(percentComplete)
            // Repeat the OCR for the present file.
            cleanupFile(files[i]!!)
            i++
        }
        if (status != RunStatus.STATUS_TERMINATED) {
            setPercentComplete(100)
        }
        Singleton.MainFrame.notifyListener(status, this)
        report()
        done()
    }

    private fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        //notify listeners
        Singleton.MainFrame.notifyListener(percentComplete, this)
        val i: MutableIterator<RunnerListener?> = listeners!!.iterator()
        while (i.hasNext()) {
            i.next().notifyListener(percentComplete, this)
        }
    }

    /**
     * Cleanup when job is complete.
     */
    private fun done() {
        Singleton.JobList.removeJob(this)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
     */
    override fun stop(): Boolean { // TODO Auto-generated method stub
        return false
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    override fun run() {
        start()
    }

    private fun report() {
        var report = "Results of directory cleanup on Image files.\n"
        report += "Found  " + counter!!.filesSeen + " image file database records.\n"
        report += "Didn't remove " + counter!!.filesExisting + " image records where file exists.\n"
        report += "Removed " + counter!!.filesFailed + " image records where file does not exist.\n"
        Singleton.MainFrame.setStatusMessage("Directory cleanup complete.")
        val errorReportDialog = RunnableJobReportDialog(Singleton.MainFrame, report, counter!!.errors, "Remove Deleted Image Results")
        errorReportDialog.setVisible(true)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = "Cleanup deleted images from a directory."

    companion object {
        /**
         * Open a dialog and scan a specific directory.
         */
        const val SCAN_SELECT = 1
        /**
         * From a file, scan a specific directory.
         */
        const val SCAN_SPECIFIC = 2
        private val log = LogFactory.getLog(JobCleanDirectory::class.java)
    }
}
