/**
 * JobRecheckForTemplates.java
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


import edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetector
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.RunStatus
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplates
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane

/**
 * JobRecheckForTemplates, recheck image files that have no template identified, but should have one,
 * and try to identify their template.  Can be run after a new template has been created.
 */
class JobRecheckForTemplates : RunnableJob, Runnable {
    var counter: Counter? = null // For reporting results
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
    private var listeners: ArrayList<RunnerListener?>? = null

    /**
     * Default constructor, scan all
     */
    constructor() {
        init(SCAN_ALL, null)
    }

    /**
     * Create a recheck for templates job to bring up dialog to pick a specific directory
     * on which to recheck image records for templates.
     *
     *
     * Behavior:
     *
     *
     * whatToScan=SCAN_ALL, all records having no template and a linked specimen are rechecked.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for repeat (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    constructor(whatToScan: Int, startAt: File?) {
        init(whatToScan, startAt)
    }

    fun init(whatToScan: Int, startAt: File?) {
        listeners = ArrayList<RunnerListener?>()
        scan = SCAN_SELECT
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt
        }
        if (whatToScan == SCAN_ALL) {
            scan = SCAN_ALL
            startPointSpecific = null
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
    }// retrieve a list of all image records with no template// retrieve a list of image records in the selected directory//TODO: handle error condition
    // Check that startPoint is or is within imagebase.
// launch a file chooser dialog to select the directory to scan// A scan start point has been provided, don't launch a dialog.// If it can't be read, null out imagebase// place to start the scan from, imagebase directory for SCAN_ALL
    // If it isn't null, retrieve the image base directory from properties, and test for read access.

    // Find the path in which to include files.
    private val fileList: MutableList<Any?>?
        private get() {
            var files: MutableList<ICImage?> = ArrayList<ICImage?>()
            if (scan != SCAN_ALL) {
                var pathToCheck = ""
                // Find the path in which to include files.
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
                            if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH) != null) {
                                fileChooser.setCurrentDirectory(File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH)))
                            }
                        }
                        val returnValue: Int = fileChooser.showOpenDialog(Singleton.getMainFrame())
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            val file: File = fileChooser.getSelectedFile()
                            log!!.debug("Selected base directory: " + file.name + ".")
                            startPoint = file
                        } else { //TODO: handle error condition
                            log!!.error("Directory selection cancelled by user.")
                        }
                    }
                    // Check that startPoint is or is within imagebase.
                    if (!ImageCaptureProperties.Companion.isInPathBelowBase(startPoint)) {
                        val base: String = Singleton.getProperties().getProperties().getProperty(
                                ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                        log!!.error("Tried to scan directory (" + startPoint!!.path + ") outside of base image directory (" + base + ")")
                        val message = "Can't scan and database files outside of base image directory ($base)"
                        JOptionPane.showMessageDialog(Singleton.getMainFrame(), message, "Can't Scan outside image base directory.", JOptionPane.YES_NO_OPTION)
                    } else {
                        if (!startPoint!!.canRead()) {
                            JOptionPane.showMessageDialog(Singleton.getMainFrame(), "Can't start scan.  Unable to read selected directory: " + startPoint.path, "Can't Scan.", JOptionPane.YES_NO_OPTION)
                        } else {
                            pathToCheck = ImageCaptureProperties.Companion.getPathBelowBase(startPoint)
                            // retrieve a list of image records in the selected directory
                            val ils = ICImageLifeCycle()
                            val pattern = ICImage()
                            pattern.setPath(pathToCheck)
                            pattern.setTemplateId(PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS)
                            files = ils.findByExample(pattern)
                            if (files != null) {
                                log!!.debug(files.size)
                            }
                        }
                    }
                }
            } else {
                try { // retrieve a list of all image records with no template
                    val ils = ICImageLifeCycle()
                    files = ICImageLifeCycle.Companion.findNotDrawerNoTemplateImages()
                    if (files != null) {
                        log!!.debug(files.size)
                    }
                } catch (e: HibernateException) {
                    log!!.error(e.message)
                    status = RunStatus.STATUS_FAILED
                    val message = "Error loading the list of images with no templates. " + e.message
                    JOptionPane.showMessageDialog(Singleton.getMainFrame(), message, "Error loading image records.", JOptionPane.YES_NO_OPTION)
                }
            }
            log!!.debug("Found " + files.size + " Image files without templates in directory to check.")
            return files
        }

    private fun recheckForTemplates(image: ICImage) {
        if (image.getSpecimen() != null) {
            val imageFile = File(assemblePathWithBase(image.getPath(), image.getFilename()))
            counter!!.incrementFilesSeen()
            val detector = ConfiguredBarcodePositionTemplateDetector()
            try {
                val ils = ICImageLifeCycle()
                val templateName: String = detector.detectTemplateForImage(imageFile)
                if (templateName != null && templateName != PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // update the image record with this template.
                    image.setTemplateId(templateName)
                    ils.attachDirty(image)
                    counter!!.incrementFilesUpdated()
                } else if (templateName != null && templateName == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
                    val error = RunnableJobError(image.getFilename(), image.getRawBarcode(),
                            "", image.getRawExifBarcode(), "No Template Found.",
                            null, null,
                            null, RunnableJobError.Companion.TYPE_NO_TEMPLATE)
                    counter!!.appendError(error)
                }
            } catch (e: UnreadableFileException) {
                log!!.error(e.message)
                val error = RunnableJobError(image.getFilename(), image.getRawBarcode(),
                        "", image.getRawExifBarcode(), "Unreadable File Exception checking for template.",
                        null, null,
                        null, RunnableJobError.Companion.TYPE_NO_TEMPLATE)
                counter!!.appendError(error)
            } catch (e: SaveFailedException) {
                log!!.error(e.message, e)
                val error = RunnableJobError(image.getFilename(), image.getRawBarcode(),
                        "", image.getRawExifBarcode(), "Save Failed Exception saving new template.",
                        null, null,
                        null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                counter!!.appendError(error)
            }
        } else {
            log!!.debug(image.getPath() + image.getFilename() + " Has no attached image.")
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        Singleton.getJobList().addJob(this)
        counter = Counter()
        // Obtain a list of image file records for the selected directory.
        val files: MutableList<ICImage?> = fileList!!
        log!!.debug("ReckeckForTemplatesJob started$this")
        var i = 0
        while (i < files.size && status != RunStatus.STATUS_TERMINATED && status != RunStatus.STATUS_FAILED) { // Find out how far along the process is
            val seen = 0.0f + i
            val total = 0.0f + files.size
            percentComplete = (seen / total * 100).toInt()
            setPercentComplete(percentComplete)
            // Repeat the OCR for the present file.
            recheckForTemplates(files[i])
            i++
        }
        if (status != RunStatus.STATUS_TERMINATED) {
            setPercentComplete(100)
        }
        Singleton.getMainFrame().notifyListener(status, this)
        report()
        done()
    }

    private fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        //notify listeners
        Singleton.getMainFrame().notifyListener(percentComplete, this)
        val i: MutableIterator<RunnerListener?> = listeners!!.iterator()
        while (i.hasNext()) {
            i.next().notifyListener(percentComplete, this)
        }
    }

    /**
     * Cleanup when job is complete.
     */
    private fun done() {
        Singleton.getJobList().removeJob(this)
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
        var report = "Results of template check on Image files missing templates (WholeImageOnly).\n"
        report += "Found  " + counter!!.filesSeen + " image file database records without templates.\n"
        report += "Updated " + counter!!.filesUpdated + " image records to a template.\n"
        Singleton.getMainFrame().setStatusMessage("Check for templates complete.")
        val errorReportDialog = RunnableJobReportDialog(Singleton.getMainFrame(), report, counter!!.errors, "Recheck Files for Templates Results")
        errorReportDialog.setVisible(true)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = "Recheck for Templates for images that are " + PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS

    companion object {
        /**
         * Recheck all files with no template.
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
        private val log = LogFactory.getLog(JobRecheckForTemplates::class.java)
    }
}
