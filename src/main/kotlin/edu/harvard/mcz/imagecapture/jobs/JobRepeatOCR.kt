/**
 * JobRepeatOCR.java
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


import edu.harvard.mcz.imagecapture.*
import edu.harvard.mcz.imagecapture.data.LocationInCollection
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.data.RunStatus
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner
import edu.harvard.mcz.imagecapture.jobs.JobRepeatOCR
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane

/**
 * JobRepeatOCR
 */
class JobRepeatOCR : RunnableJob, Runnable {
    var counter: Counter? = null // For reporting results
    var listeners: ArrayList<RunnerListener?>? = null
    private var scan = SCAN_ALL // default scan all
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
     * Default constructor.  Creates an OCR job to repeat the OCR on all
     * images for all specimens in state OCR.
     */
    constructor() {
        scan = SCAN_ALL
        startPointSpecific = null
        status = RunStatus.STATUS_NEW
        init()
    }

    /**
     * Create a repeat OCR job to bring up dialog to pick a specific directory
     * on which to repeat OCR for specimens in state OCR or to repeat OCR
     * for a specific directory specified by startAt, again for specimens in
     * state OCR.
     *
     *
     * Behavior:
     *
     *
     * whatToScan=SCAN_ALL, startAt is ignored, equivalent to default constructor.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for repeat (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_ALL, SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    constructor(whatToScan: Int, startAt: File?) {
        scan = SCAN_SELECT
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt
        }
        if (whatToScan == SCAN_ALL) { // equivalent to default constructor
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
        init()
    }

    protected fun init() {
        listeners = ArrayList<RunnerListener?>()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        log!!.debug("JobRepeatOCR $this  recieved cancel signal")
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
        if (listeners == null) {
            init()
        }
        log!!.debug(jobListener)
        return listeners!!.add(jobListener)
    }// Add image for specimen to list to check// run in separate thread and allow cancellation and status reporting
// walk through directory tree
    // Retrieve a list of all specimens in state OCR
//TODO: handle error condition
    //TODO: Filechooser to pick path, then save (if SCAN_ALL) imagebase property.
//Perhaps.  Might be undesirable behavior.
//Probably better to warn that imagebase is null;
    // Check that startPoint is or is within imagebase.
// launch a file chooser dialog to select the directory to scan// A scan start point has been provided, don't launch a dialog.// If it can't be read, null out imagebase// place to start the scan from, imagebase directory for SCAN_ALL
    // If it isn't null, retrieve the image base directory from properties, and test for read access.

    // Find the path in which to include files.
    private val fileList: MutableList<File?>
        private get() {
            var pathToCheck = ""
            if (scan != SCAN_ALL) { // Find the path in which to include files.
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
                        //TODO: Filechooser to pick path, then save (if SCAN_ALL) imagebase property.
//Perhaps.  Might be undesirable behavior.
//Probably better to warn that imagebase is null;
                    }
                    // Check that startPoint is or is within imagebase.
                    if (!ImageCaptureProperties.Companion.isInPathBelowBase(startPoint)) {
                        val base: String = Singleton.Properties.Properties.getProperty(
                                ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                        log!!.error("Tried to scan directory (" + startPoint!!.path + ") outside of base image directory (" + base + ")")
                        val message = "Can't scan and database files outside of base image directory ($base)"
                        JOptionPane.showMessageDialog(Singleton.MainFrame, message, "Can't Scan outside image base directory.", JOptionPane.YES_NO_OPTION)
                    } else { // run in separate thread and allow cancellation and status reporting
// walk through directory tree
                        if (!startPoint!!.canRead()) {
                            JOptionPane.showMessageDialog(Singleton.MainFrame, "Can't start scan.  Unable to read selected directory: " + startPoint.path, "Can't Scan.", JOptionPane.YES_NO_OPTION)
                        } else {
                            pathToCheck = ImageCaptureProperties.Companion.getPathBelowBase(startPoint)
                        }
                    }
                }
            }
            // Retrieve a list of all specimens in state OCR
            val sls = SpecimenLifeCycle()
            val specimens: MutableList<Specimen?> = sls.findBy("workFlowStatus", WorkFlowStatus.STAGE_0)
            val files = ArrayList<File?>()
            for (i in specimens.indices) {
                val s: Specimen? = specimens[i]
                val images: MutableSet<ICImage?> = s.ICImages
                val iter: MutableIterator<ICImage?> = images.iterator()
                while (iter.hasNext() && status != RunStatus.STATUS_TERMINATED) {
                    val image: ICImage? = iter.next()
                    if (scan == SCAN_ALL || image.Path.startsWith(pathToCheck)) { // Add image for specimen to list to check
                        val imageFile = File(assemblePathWithBase(image.Path, image.Filename))
                        files.add(imageFile)
                        counter!!.incrementFilesSeen()
                    }
                }
            }
            val message = "Found " + files.size + " Specimen records on which to repeat OCR."
            log!!.debug(message)
            Singleton.MainFrame.setStatusMessage(message)
            return files
        }

    private fun redoOCR(file: File) {
        log!!.debug(file)
        val filename = file.name
        var rawOCR = ""
        // scan selected file
        var templateToUse: PositionTemplate? = null
        // Figure out which template to use.
        val detector = DefaultPositionTemplateDetector()
        var template = ""
        try {
            template = detector.detectTemplateForImage(file)
        } catch (e3: UnreadableFileException) { // TODO Auto-generated catch block
            e3.printStackTrace()
        }
        try {
            templateToUse = PositionTemplate(template)
            log.debug("Set template to: " + templateToUse.TemplateId)
        } catch (e1: NoSuchTemplateException) {
            try {
                templateToUse = PositionTemplate(PositionTemplate.Companion.TEMPLATE_DEFAULT)
                log.error("Template not recongised, reset template to: " + templateToUse.TemplateId)
            } catch (e2: Exception) { // We shouldn't end up here - we just asked for the default template by its constant.
                log.fatal("PositionTemplate doesn't recognize TEMPLATE_DEFAULT")
                log.trace(e2)
                ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR)
            }
        }
        Singleton.MainFrame.setStatusMessage("Repeat OCR $filename.")
        val scannableFile: CandidateImageFile?
        try {
            scannableFile = CandidateImageFile(file, templateToUse)
            try { // OCR and parse UnitTray Label
                var parser: TaxonNameReturner? = null
                var labelRead: UnitTrayLabel? = null
                var foundQRText = false
                try {
                    labelRead = scannableFile.getTaxonLabelQRText(PositionTemplate("Test template 2"))
                } catch (e: NoSuchTemplateException) {
                    try {
                        labelRead = scannableFile.getTaxonLabelQRText(PositionTemplate("Small template 2"))
                    } catch (e1: NoSuchTemplateException) {
                        log.error("Neither Test template 2 nor Small template 2 found")
                    }
                }
                if (labelRead != null) {
                    rawOCR = labelRead.toJSONString()
                    foundQRText = true
                    parser = labelRead
                } else {
                    log.debug("Failing over to OCR with tesseract")
                    rawOCR = scannableFile.getTaxonLabelOCRText(templateToUse)
                    parser = UnitTrayLabelParser(rawOCR)
                    foundQRText = (parser as UnitTrayLabelParser?).isParsedFromJSON()
                }
                log.debug(rawOCR)
                // Test this image to see if is a specimen image
                var barcode: String = scannableFile.getBarcodeText(templateToUse)
                Singleton.MainFrame.setStatusMessage("Checking $barcode.")
                if (scannableFile.CatalogNumberBarcodeStatus != CandidateImageFile.Companion.RESULT_BARCODE_SCANNED) {
                    log.error("Error scanning for barcode: $barcode")
                    barcode = ""
                }
                println("Barcode=$barcode")
                val exifComment: String = scannableFile.ExifUserCommentText
                var isSpecimenImage = false
                if (Singleton.BarcodeMatcher.matchesPattern(exifComment)
                        || Singleton.BarcodeMatcher.matchesPattern(barcode)) {
                    isSpecimenImage = true
                    println("Specimen Image")
                }
                var rawBarcode = barcode
                // Check for mismatch in barcode and comment
                if (rawBarcode != exifComment) { // Use the exifComment if it is a barcode
                    var barcodeInImageMetadata = false
                    if (Singleton.BarcodeMatcher.matchesPattern(exifComment)) {
                        rawBarcode = exifComment
                        barcodeInImageMetadata = true
                    }
                    // Log the mismatch
                    logMismatch(counter, filename, barcode, exifComment, parser, barcodeInImageMetadata, log)
                }
                if (isSpecimenImage && Singleton.BarcodeMatcher.matchesPattern(rawBarcode)) { // Parse and store OCR in an updated specimen record.
                    Singleton.MainFrame.setStatusMessage("Updating $barcode.")
                    val sls = SpecimenLifeCycle()
                    val specimenSearch = Specimen()
                    specimenSearch.setBarcode(rawBarcode)
                    val specimens: MutableList<Specimen?> = sls.findByExample(specimenSearch)
                    log.debug("Found " + specimens.size + " for barcode " + rawBarcode)
                    if (specimens.size == 1) { // Only update if we got a single match back on the barcode search.
                        var s: Specimen? = specimens[0]
                        log.debug("Found " + s.Barcode + " at state " + s.WorkFlowStatus)
                        if (s.WorkFlowStatus == WorkFlowStatus.STAGE_0) { // Only update if the result was in state OCR.
//
// Look up likely matches for the OCR of the higher taxa in the HigherTaxon authority file.
                            AbstractFileScanJob.extractFamilyToSpecimen(parser, s)
                            if (parser.Family != "") { // check family against database (with a soundex match)
                                val hls = HigherTaxonLifeCycle()
                                val match: String = hls.findMatch(parser.Family)
                                if (match != null && match.trim { it <= ' ' } != "") {
                                    s.setFamily(match)
                                }
                            }
                            // trim family to fit (in case multiple parts of taxon name weren't parsed
// and got concatenated into family field.
                            setBasicSpecimenFromParser(parser, s)
                            if (s.CreatingPath == null || s.CreatingPath.length == 0) {
                                s.setCreatingPath(ImageCaptureProperties.Companion.getPathBelowBase(file))
                            }
                            if (s.CreatingFilename == null || s.CreatingFilename.length == 0) {
                                s.setCreatingFilename(file.name)
                            }
                            if (parser.IdentifiedBy != null && parser.IdentifiedBy.length > 0) {
                                s.setIdentifiedBy(parser.IdentifiedBy)
                            }
                            log.debug(s.Collection)
                            // TODO: non-general workflows
                            s.setLocationInCollection(LocationInCollection.DefaultLocation)
                            if (s.Family == "Formicidae") {
                                s.setLocationInCollection(LocationInCollection.GENERALANT)
                            }
                            s.setCreatedBy(ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.AppVersion)
                            val sh = SpecimenLifeCycle()
                            try { // *** Save a database record of the specimen.
                                log.debug("Saving changes for barcode $barcode")
                                if (foundQRText) { // if we managed to read JSON, then we can move the specimen to text entered.
                                    s.setWorkFlowStatus(WorkFlowStatus.STAGE_1)
                                    log.debug(s.WorkFlowStatus)
                                }
                                sh.attachDirty(s)
                                counter!!.incrementSpecimensUpdated()
                            } catch (e: SaveFailedException) { // couldn't save, try to figure out why and report
                                log.debug(e)
                                try {
                                    var badParse = ""
                                    // Drawer number with length limit (and specimen that fails to save at over this length makes
// a good canary for labels that parse very badly.
                                    if ((parser as DrawerNameReturner?).DrawerNumber.length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) {
                                        badParse = "Parsing problem. \nDrawer number is too long: " + s.DrawerNumber + "\n"
                                    }
                                    val error = RunnableJobError(filename, barcode,
                                            rawBarcode, exifComment, badParse,
                                            parser, parser as DrawerNameReturner?,
                                            null, RunnableJobError.Companion.TYPE_BAD_PARSE)
                                    counter!!.appendError(error)
                                } catch (err: Exception) {
                                    log.error(e)
                                    log.error(err)
                                    // TODO: Add a general error handling/inform user class.
                                    var badParse = ""
                                    // Drawer number with length limit (and specimen that fails to save at over this length makes
// a good canary for labels that parse very badly.
                                    if (s.DrawerNumber == null) {
                                        badParse = "Parsing problem. \nDrawer number is null: \n"
                                    } else {
                                        if (s.DrawerNumber.length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) { // This was an OK test for testing OCR, but in production ends up in records not being
// created for files, which ends up being a larger quality control problem than records
// with bad OCR.
// Won't fail this way anymore - drawer number is now enforced in Specimen.setDrawerNumber()
                                            badParse = "Parsing problem. \nDrawer number is too long: " + s.DrawerNumber + "\n"
                                        }
                                    }
                                    val error = RunnableJobError(filename, barcode,
                                            rawBarcode, exifComment, badParse,
                                            parser, parser as DrawerNameReturner?,
                                            err, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                                    counter!!.appendError(error)
                                    counter!!.incrementFilesFailed()
                                    s = null
                                }
                            }
                        } else {
                            log.debug("Didn't try to save, not at workflow status OCR.")
                            val error = RunnableJobError(filename, barcode,
                                    rawBarcode, exifComment, "Didn't try to save, not at workflow status OCR",
                                    parser, parser as DrawerNameReturner?,
                                    null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                            counter!!.appendError(error)
                        }
                    }
                } else {
                    log.debug("Didn't try to save, not a specimen image.")
                    val error = RunnableJobError(filename, barcode,
                            rawBarcode, exifComment, "Didn't try to save, not a specimen image, or rawBarcode doesn't match pattern",
                            parser, parser as DrawerNameReturner?,
                            null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                    counter!!.appendError(error)
                }
            } catch (ex: Exception) {
                println(ex.message)
            }
        } catch (e1: UnreadableFileException) {
            log.error("Unable to read selected file." + e1.message)
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        Singleton.JobList.addJob(this)
        counter = Counter()
        // Obtain a list of image files to repeat OCR
// (by querying specimens in state OCR and getting list of linked images).
        val files = fileList
        log!!.debug("repeatOCRJob started$this")
        var i = 0
        while (i < files.size && status != RunStatus.STATUS_TERMINATED) { // Find out how far along the process is
            val seen = 0.0f + i
            val total = 0.0f + files.size
            percentComplete = (seen / total * 100).toInt()
            setPercentComplete(percentComplete)
            // Repeat the OCR for the present file.
            redoOCR(files[i]!!)
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
        log!!.debug(percentComplete)
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
        var report = "Results of redone OCR on Image files.\n"
        report += "Found  " + counter!!.filesSeen + " specimen database records in state OCR.\n"
        report += "Saved new OCR for " + counter!!.specimensUpdated + " specimens.\n"
        Singleton.MainFrame.setStatusMessage("OCR re-do complete.")
        val errorReportDialog = RunnableJobReportDialog(Singleton.MainFrame, report, counter!!.errors, "Repeat OCR Results")
        errorReportDialog.setVisible(true)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = if (scan == SCAN_ALL) {
            "Redo OCR for All specimens in state " + WorkFlowStatus.STAGE_0
        } else {
            "Redo OCR for specimens in state " + WorkFlowStatus.STAGE_0
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
        private val log = LogFactory.getLog(JobRepeatOCR::class.java)
    }
}
