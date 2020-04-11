/**
 * JobFileReconciliation.java
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
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.JobFileReconciliation
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModel
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import javax.swing.JOptionPane

/**
 * JobFileReconciliation, walk through the image directories and find all files that have a filename
 * pattern that matches that of an image file that should be in the database, check if they are, and
 * report if they are not present in the database.
 */
class JobFileReconciliation : RunnableJob, Runnable {
    var resultCounter: Counter? = null
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
    private var listeners: ArrayList<RunnerListener?>? = null
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        return false
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#percentComplete()
     */
    override fun percentComplete(): Int { // TODO Auto-generated method stub
        return 0
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#registerListener(edu.harvard.mcz.imagecapture.interfaces.RunnerListener)
     */
    override fun registerListener(jobListener: RunnerListener?): Boolean {
        if (listeners == null) {
            listeners = ArrayList<RunnerListener?>()
        }
        return listeners!!.add(jobListener)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        status = RunStatus.STATUS_RUNNING
        Singleton.getJobList().addJob(this)
        resultCounter = Counter()
        reconcileFiles()
        report(resultCounter!!)
        done()
    }

    private fun reconcileFiles() { // find the place to start
        var imagebase: File? = null // place to start the scan from, imagebase directory for SCAN_ALL
        var startPoint: File? = null
        // If it isn't null, retrieve the image base directory from properties, and test for read access.
        if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE) == null) {
            JOptionPane.showMessageDialog(Singleton.getMainFrame(), "Can't start scan.  Don't know where images are stored.  Set imagbase property.", "Can't Scan.", JOptionPane.ERROR_MESSAGE)
        } else {
            imagebase = File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
            if (imagebase != null) {
                if (imagebase.canRead()) {
                    startPoint = imagebase
                    // recurse through directory tree from place to start
                    checkFiles(startPoint, resultCounter!!)
                }
            }
        }
    }

    private fun checkFiles(placeToStart: File, counter: Counter) { // get a list of files in placeToStart
        val containedFiles = placeToStart.listFiles()
        if (containedFiles != null) {
            for (i in containedFiles.indices) {
                if (status != RunStatus.STATUS_TERMINATED) {
                    val fileToCheck = containedFiles[i]
                    Singleton.getMainFrame().setStatusMessage("Reconciling: " + fileToCheck!!.name)
                    // Check to see if this is a directory
                    if (fileToCheck.isDirectory) {
                        if (fileToCheck.name == "thumbs") {
                            log!!.debug("Skipping thumbnail directory: " + fileToCheck.path + File.separator + fileToCheck.name)
                        } else {
                            if (fileToCheck.canRead()) {
                                checkFiles(fileToCheck, counter)
                                counter.incrementDirectories()
                            } else {
                                counter.incrementDirectoriesFailed()
                            }
                        }
                    } else { // fileToCheck is a file.
// does file to check match pattern of an image file.
                        if (fileToCheck.name.matches(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEREGEX))) { // it is an image file.
                            counter.incrementFilesSeen()
                            // Check to see if this file is in the database.
                            val dbImage = ICImage()
                            dbImage.setFilename(fileToCheck.name)
                            dbImage.setPath(ImageCaptureProperties.Companion.getPathBelowBase(fileToCheck))
                            val ils = ICImageLifeCycle()
                            val matches: MutableList<ICImage?> = ils.findByExample(dbImage)
                            if (matches != null && matches.size == 1) { //if it is, increment the found counter
                                counter.incrementFilesDatabased()
                            } else if (matches != null && matches.size > 1) {
                                val barcode = StringBuffer()
                                val ri: MutableIterator<ICImage?> = matches.iterator()
                                while (ri.hasNext()) {
                                    val match: ICImage? = ri.next()
                                    barcode.append(match.getSpecimen().getBarcode()).append(" ")
                                }
                                counter.incrementFilesDatabased()
                                log!!.error("File with more than one database match by name and path")
                                val error = RunnableJobError(fileToCheck.name, barcode.toString().trim { it <= ' ' },
                                        ImageCaptureProperties.Companion.getPathBelowBase(fileToCheck), "", "More than one database (Image) record for this file.",
                                        null, null,
                                        null, RunnableJobError.Companion.TYPE_DUPLICATE)
                                counter.appendError(error)
                            } else {
                                val barcode = StringBuffer()
                                if (matches != null) {
                                    val ri: MutableIterator<ICImage?> = matches.iterator()
                                    while (ri.hasNext()) {
                                        val match: ICImage? = ri.next()
                                        barcode.append(match.getSpecimen().getBarcode()).append(" ")
                                    }
                                }
                                counter.incrementFilesFailed()
                                val error = RunnableJobError(fileToCheck.name, barcode.toString().trim { it <= ' ' },
                                        ImageCaptureProperties.Companion.getPathBelowBase(fileToCheck), "", "No database (Image) record for this file.",
                                        null, null,
                                        null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                                counter.appendError(error)
                            }
                        }
                    } // end if directory else file
                } // end status terminated check
            } // end for loop
        } // end contained files not null check
    }

    private fun report(counter: Counter) {
        var report = "Image file to database image record reconciliation.\n"
        report += "Scanned  " + counter.directories + " directories.\n"
        report += "Found  " + counter.filesSeen + " image files.\n"
        report += "Found  " + counter.filesDatabased + " image file database records.\n"
        report += "Found " + counter.filesFailed + " image files not in the database.\n"
        Singleton.getMainFrame().setStatusMessage("File Reconciliation check complete")
        val errorReportDialog = RunnableJobReportDialog(Singleton.getMainFrame(),
                report,
                counter.errors,
                RunnableJobErrorTableModel.Companion.TYPE_FILE_RECONCILIATION,
                "Image File/Image Record reconciliation Results")
        errorReportDialog.setVisible(true)
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

    /**
     * Cleanup when job is complete.
     */
    private fun done() {
        Singleton.getJobList().removeJob(this)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = "Reconcile Image Files with Database"

    companion object {
        private val log = LogFactory.getLog(JobFileReconciliation::class.java)
    }
}
