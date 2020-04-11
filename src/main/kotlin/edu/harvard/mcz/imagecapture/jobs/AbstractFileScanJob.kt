package edu.harvard.mcz.imagecapture.jobs


import edu.harvard.mcz.imagecapture.*
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import edu.harvard.mcz.imagecapture.interfaces.*
import edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJob
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.locks.Lock

abstract class AbstractFileScanJob : RunnableJob, Runnable {
    /**
     * @see java.lang.Runnable.run
     */
    override fun run() {
        start()
    }

    /**
     * Cleanup when job is complete.
     */
    protected fun done() {
        Singleton.JobList.removeJob(this)
    }

    companion object {
        private val log = LogFactory.getLog(AbstractFileScanJob::class.java)
        /**
         * Log errors associated with confusion between exifComment & detected barcode
         *
         * @param counter                to apply the error to
         * @param filename               the errored file
         * @param barcode                the barcode as detected
         * @param exifComment            the comment, exif as read from file
         * @param parser                 the parser used
         * @param barcodeInImageMetadata whether the barcode was extracted from metadata
         * @param log
         */
        fun logMismatch(counter: ScanCounterInterface, filename: String?, barcode: String?, exifComment: String?, parser: TaxonNameReturner?, barcodeInImageMetadata: Boolean, log: Log) {
            if (barcodeInImageMetadata || Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_REDUNDANT_COMMENT_BARCODE) == "true") { // If so configured, or if image metadata contains a barcode that doesn't match the barcode in the image
// report on barcode/comment missmatch as an error condition.
                try {
                    val error = RunnableJobError(filename, barcode,
                            barcode, exifComment, "Barcode/Comment mismatch.",
                            parser, parser as DrawerNameReturner?,
                            null, RunnableJobError.Companion.TYPE_MISMATCH)
                    counter.appendError(error)
                } catch (e: Exception) { // we don't want an exception to stop processing
                    log.error(e)
                }
            } else { // Just write into debug log
// This would normally the case where the image metadata doesn't contain a barcode but the image does, and reporting of this state as an error has been turned off.
                log.debug("Barcode/Comment mismatch: [$barcode]!=[$exifComment]")
            }
        }

        /**
         * Set family, subfamily, based on a taxon returner
         *
         * @param parser source of family, subfamily
         * @param s      the specimen to set the values on
         */
        fun extractFamilyToSpecimen(parser: TaxonNameReturner, s: Specimen) {
            val hls = HigherTaxonLifeCycle()
            if (parser.Tribe.trim({ it <= ' ' }) == "") {
                if (hls.isMatched(parser.Family, parser.Subfamily)) { // If there is a match, use it.
                    val higher: Array<String?> = hls.findMatch(parser.Family, parser.Subfamily)
                    s.setFamily(higher[0])
                    s.setSubfamily(higher[1])
                } else { // otherwise use the raw OCR output.
                    s.setFamily(parser.Family)
                    s.setSubfamily(parser.Subfamily)
                }
                s.setTribe("")
            } else {
                if (hls.isMatched(parser.Family, parser.Subfamily, parser.Tribe)) {
                    val higher: Array<String?> = hls.findMatch(parser.Family, parser.Subfamily, parser.Tribe)
                    s.setFamily(higher[0])
                    s.setSubfamily(higher[1])
                    s.setTribe(higher[2])
                } else {
                    s.setFamily(parser.Family)
                    s.setSubfamily(parser.Subfamily)
                    s.setTribe(parser.Tribe)
                }
            }
        }

        /**
         * Do actual processing of one file.
         * Static to enable threaded, state-less execution
         *
         * @param containedFile the file to process
         * @param counter       the counter to keep track of success/failure
         * @param locks         the locks to use to prevent concurrency issues
         */
        protected fun checkFile(containedFile: File, counter: ScanCounterInterface, locks: Array<Lock?>?) {
            Singleton.Properties.Properties.setProperty(ImageCaptureProperties.Companion.KEY_LASTPATH, containedFile.path)
            val filename = containedFile.name
            counter.incrementFilesSeen()
            log!!.debug("Checking image file: $filename")
            CandidateImageFile.Companion.debugCheckHeightWidth(containedFile)
            // scan file for barcodes and ocr of unit tray label text
            try {
                var reattach = false // image is detached instance and should be reattached instead of persisted denovo.
                // Check for an existing image record.
                val imageLifeCycle = ICImageLifeCycle()
                var existingTemplate = ICImage()
                existingTemplate.setFilename(filename)
                val path: String = ImageCaptureProperties.Companion.getPathBelowBase(containedFile)
                existingTemplate.setPath(path)
                val matches: MutableList<ICImage?> = imageLifeCycle.findBy(object : HashMap<String?, Any?>() {
                    init {
                        put("path", path)
                        put("filename", filename)
                    }
                })
                log.debug((matches?.size ?: "no").toString() + " matches found for " + filename)
                if (matches != null && matches.size == 1 && matches[0].RawBarcode == null && matches[0].RawExifBarcode == null && (matches[0].DrawerNumber == null || matches[0].DrawerNumber.trim({ it <= ' ' }).length == 0)) { // likely case for a failure to read data out of the image file
// try to update the image file record.
                    try {
                        existingTemplate = imageLifeCycle.merge(matches[0])
                        matches.removeAt(0)
                        reattach = true
                        log.debug(existingTemplate)
                    } catch (e: SaveFailedException) {
                        log.error(e.message, e)
                    }
                } else if (matches != null && matches.size == 1 && matches[0].Specimen == null) { // likely case for a failure to create a specimen record in a previous run
// try to update the image file record
                    try {
                        existingTemplate = imageLifeCycle.merge(matches[0])
                        matches.removeAt(0)
                        reattach = true
                        log.debug(existingTemplate)
                    } catch (e: SaveFailedException) {
                        log.error(e.message, e)
                    }
                }
                if (matches == null || matches.size == 0) {
                    createDatabaseRecordForFile(containedFile, counter, reattach, imageLifeCycle, existingTemplate, locks)
                } else { // found an already databased file (where we have barcode/specimen or drawer number data).
                    log.debug("Record exists, skipping file $filename")
                    counter.incrementFilesExisting()
                }
            } catch (e: UnreadableFileException) {
                counter.incrementFilesFailed()
                counter.appendError(RunnableJobError(containedFile.name, "", "Could not read file", UnreadableFileException(), RunnableJobError.Companion.TYPE_FILE_READ))
                log.error("Couldn't read file " + filename + "." + e.message)
            }
        }

        /**
         * Create a new image database record.
         * Static to enable threaded, state-less execution
         *
         * @param containedFile  the file path relative to the start
         * @param counter        to count errors
         * @param reattach       whether there is already a database record existing, to be overridden
         * @param imageLifeCycle the repository to sage to
         * @param image          providing access to path & filename
         * @throws UnreadableFileException if the file could not be read
         */
        @Throws(UnreadableFileException::class)
        protected fun createDatabaseRecordForFile(containedFile: File, counter: ScanCounterInterface, reattach: Boolean, imageLifeCycle: ICImageLifeCycle, image: ICImage, locks: Array<Lock?>?) {
            var reattach = reattach
            var isSpecimenImage = false
            var isDrawerImage = false
            val matcher: BarcodeMatcher = Singleton.BarcodeMatcher
            // ** Identify the template.
// String templateId = detector.detectTemplateForImage(fileToCheck);
// log.debug("Detected Template: " + templateId);
// PositionTemplate template = new PositionTemplate(templateId);
// // Found a barcode in a templated position in the image.
// // ** Scan the file based on this template.
// candidateImageFile = new CandidateImageFile(fileToCheck, template);
// Construct a CandidateImageFile with constructor that self detects template
            val candidateImageFile = CandidateImageFile(containedFile)
            val template: PositionTemplate = candidateImageFile.TemplateUsed
            val templateId: String = template.Name
            log!!.debug("Detected Template: $templateId")
            log.debug(candidateImageFile.CatalogNumberBarcodeStatus)
            var barcode: String = candidateImageFile.BarcodeTextAtFoundTemplate
            if (candidateImageFile.CatalogNumberBarcodeStatus != CandidateImageFile.Companion.RESULT_BARCODE_SCANNED) {
                log.error("Error scanning for barcode on file '" + containedFile.name + "': " + barcode)
                barcode = ""
            }
            log.debug("Barcode: of file '" + containedFile.name + "':" + barcode)
            val exifComment: String = candidateImageFile.ExifUserCommentText
            log.debug("ExifComment: $exifComment")
            val labelRead: UnitTrayLabel = candidateImageFile.getTaxonLabelQRText(template)
            val parser: TaxonNameReturner?
            var rawOCR: String
            val state: String
            if (labelRead != null) {
                rawOCR = labelRead.toJSONString()
                log.debug("UnitTrayLabel: of file '" + containedFile.name + "':" + rawOCR)
                state = WorkFlowStatus.STAGE_1
                parser = labelRead
            } else {
                try {
                    rawOCR = candidateImageFile.getTaxonLabelOCRText(template)
                } catch (e: OCRReadException) {
                    log.error(e)
                    rawOCR = ""
                    log.error("Couldn't OCR file '" + containedFile.name + "':." + e.message)
                    val error = RunnableJobError(image.Filename, "OCR Failed",
                            barcode, exifComment, "Couldn't find text to OCR for taxon label.",
                            null, null,
                            e, RunnableJobError.Companion.TYPE_NO_TEMPLATE)
                    counter.appendError(error)
                }
                if (rawOCR == null) {
                    rawOCR = ""
                }
                state = WorkFlowStatus.STAGE_0
                parser = UnitTrayLabelParser(rawOCR)
                // Provide error message to distinguish between entirely OCR or
                if ((parser as UnitTrayLabelParser?).isParsedFromJSON()) {
                    val error = RunnableJobError(image.Filename, "OCR Failover found barcode.",
                            barcode, exifComment, "Couldn't read Taxon barcode, failed over to OCR, but OCR found taxon barcode.",
                            parser, null,
                            null, RunnableJobError.Companion.TYPE_FAILOVER_TO_OCR)
                    counter.appendError(error)
                } else {
                    val error = RunnableJobError(image.Filename, "TaxonLabel read failed.",
                            barcode, exifComment, "Couldn't read Taxon barcode, failed over to OCR only.",
                            parser, null,
                            null, RunnableJobError.Companion.TYPE_FAILOVER_TO_OCR)
                    counter.appendError(error)
                }
            }
            // Test: is exifComment a barcode:-
// Case 1: This is an image of papers associated with a container (a unit tray or a box).
// This case can be identified by there being no barcode data associated with the image.
// Action:
// A) Check the exifComment to see what metadata is there, if blank, User needs to fix.
//    exifComment may contain a drawer number, identifying this as a drawer image.  Save as such.
// Options: A drawer, for which number is captured.  A unit tray, capture ?????????.  A specimen
// where barcode wasn't read, allow capture of barcode and treat as Case 2.
// B) Create an image record and store the image metadata (with a null specimen_id).
// Case 2: This is an image of a specimen and associated labels or an image assocated with
// a specimen with the specimen's barcode label in the image.
// This case can be identified by there being a barcode in a templated position or there
// being a barcode in the exif comment tag.
// Action:
// A) Check if a specimen record exists, if not, create one from the barcode and OCR data.
// B) Create an image record and store the image metadata.
            if (matcher.matchesPattern(exifComment)
                    || matcher.matchesPattern(barcode)) {
                isSpecimenImage = true
                println(containedFile.name + " is a Specimen Image")
            } else {
                if (exifComment != null && exifComment.matches(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                    isDrawerImage = true
                    println(containedFile.name + " is a Drawer Image")
                } else { // no way we could continue from here on
                    val errorType: Int = if (templateId == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) RunnableJobError.Companion.TYPE_NO_TEMPLATE else RunnableJobError.Companion.TYPE_UNKNOWN
                    val error = RunnableJobError(image.Filename, barcode,
                            barcode, exifComment, "Image doesn't appear to contain a barcode in a templated position.",
                            null, null,
                            null, errorType)
                    counter.appendError(error)
                    counter.incrementFilesFailed()
                    return
                }
            }
            image.setRawBarcode(barcode)
            if (isSpecimenImage) {
                createDatabaseRecordForSpecimen(containedFile, counter, image, barcode, exifComment, parser, labelRead, state, locks)
                // reattach as we force a save by having cascade= all
                reattach = true
            }
            if (isDrawerImage) {
                image.setDrawerNumber(exifComment)
            } else {
                image.setRawExifBarcode(exifComment)
                image.setDrawerNumber((parser as DrawerNameReturner?).DrawerNumber)
            }
            image.setRawOcr(rawOCR)
            image.setTemplateId(template.TemplateId)
            image.setPath(image.Path)
            // Create md5hash of image file, persist with image
            if (image.getMd5sum() == null || image.getMd5sum().length == 0) {
                try {
                    image.setMd5sum(DigestUtils.md5Hex(FileInputStream(containedFile)))
                } catch (e: IOException) {
                    log.error(e.message)
                }
            }
            try {
                if (reattach) { // Update image file record
                    imageLifeCycle.attachDirty(image)
                    log.debug("Updated " + image.toString())
                    counter.incrementFilesUpdated()
                } else { // *** Save a database record of the image file.
                    imageLifeCycle.persist(image)
                    log.debug("Saved " + image.toString())
                    counter.incrementFilesDatabased()
                }
            } catch (e: SaveFailedException) {
                log.error(e.message, e)
                counter.incrementFilesFailed()
                val failureMessage = "Failed to save image record for image " + containedFile.name + ".  " + e.message
                val error = RunnableJobError(image.Filename, "Save Failed",
                        image.Filename, image.Path, failureMessage,
                        null, null,
                        null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                counter.appendError(error)
            }
            if (isSpecimenImage) {
                if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_REDUNDANT_COMMENT_BARCODE) == "true") { // If so configured, log as error
                    if (image.RawBarcode != image.RawExifBarcode) {
                        log.error("Warning: Scanned Image '" + containedFile.name + "' has missmatch between barcode and comment.")
                    }
                }
            }
        }

        /**
         * Create or update an existing Specimen record to associate it with the ICImage.
         * Static to enable threaded, state-less execution.
         * BUT, since multiple images could reference the same specimen, we have to lock stuff
         *
         * @param containedFile the file of the image
         * @param counter       counter to add errors
         * @param image         the image to associate with the specimen
         * @param barcode       the barcode associated with the specimen
         * @param exifComment   the exif metadata comment to enable additional data extraction
         * @param unitTrayLabel the unit tray label of the Specimen
         * @param state         the workflow state we are in with the specimen/image
         */
        protected fun createDatabaseRecordForSpecimen(containedFile: File, counter: ScanCounterInterface, image: ICImage, barcode: String?, exifComment: String?, parser: TaxonNameReturner, unitTrayLabel: UnitTrayLabel?, state: String, locks: Array<Lock?>?) {
            var barcode = barcode
            val matcher: BarcodeMatcher = Singleton.BarcodeMatcher
            val rawBarcode = barcode
            if (rawBarcode != exifComment) { // Log the missmatch
                logMismatch(counter, image.Filename, barcode, exifComment, parser, matcher.matchesPattern(exifComment), log!!)
            }
            Singleton.MainFrame.setStatusMessage("Creating new specimen record for file " + containedFile.name + ".")
            var s: Specimen? = Specimen()
            if (!matcher.matchesPattern(barcode)
                    && matcher.matchesPattern(exifComment)) { // special case: couldn't read QR code barcode from image, but it was present in exif comment.
                s.setBarcode(exifComment)
                barcode = exifComment
            } else {
                if (!matcher.matchesPattern(barcode)) { // Won't be able to save the specimen record if we end up here.
                    log!!.error("Neither exifComment nor QR Code barcode match the expected pattern for a barcode, but isSpecimenImage got set to true.")
                }
                s.setBarcode(barcode)
            }
            // check if there already exists a specimen to add the image to
// but make sure you are the only to try.
            val lock = locks!![barcode.hashCode() and locks.size - 1]
            if (lock == null) {
                log!!.debug("Lock null, fetched from " + barcode.hashCode() + ", " + locks.size + " and " + (barcode.hashCode() and locks.size - 1))
            }
            lock!!.lock()
            try {
                val specimenLifeCycle = SpecimenLifeCycle()
                val existingSpecimens: MutableList<Specimen?> = specimenLifeCycle.findByBarcode(s.Barcode)
                if (existingSpecimens != null && existingSpecimens.size > 0) {
                    counter.incrementSpecimenExisting()
                    assert(existingSpecimens.size == 1)
                    for (specimen in existingSpecimens) {
                        image.setSpecimen(specimen)
                        specimen.ICImages.add(image)
                        try {
                            specimenLifeCycle.attachDirty(specimen)
                        } catch (e: SaveFailedException) {
                            counter.appendError(RunnableJobError(containedFile.name, barcode, specimen.SpecimenId.toString(), "Failed adding image to existing Specimen", e, RunnableJobError.Companion.TYPE_SAVE_FAILED))
                        }
                    }
                    return
                }
                s.setWorkFlowStatus(state)
                if (unitTrayLabel != null) { //  We got json data from a barcode.
                    s.setFamily(parser.Family)
                    s.setSubfamily(parser.Subfamily)
                    s.setTribe(parser.Tribe)
                } else { // We failed over to OCR, try lookup in DB.
                    s.setFamily("") // make sure there's a a non-null value in family.
                    extractFamilyToSpecimen(parser, s)
                }
                if (state == WorkFlowStatus.STAGE_0) { // Look up likely matches for the OCR of the higher taxa in the HigherTaxon authority file.
                    if (parser.Family != "") { // check family against database (with a soundex match)
                        val hls = HigherTaxonLifeCycle()
                        val match: String = hls.findMatch(parser.Family)
                        if (match != null && match.trim { it <= ' ' } != "") {
                            s.setFamily(match)
                        }
                    }
                }
                // trim family to fit (in case multiple parts of taxon name weren't parsed
// and got concatenated into family field.
                setBasicSpecimenFromParser(parser, s)
                s.setCreatingPath(ImageCaptureProperties.Companion.getPathBelowBase(containedFile))
                s.setCreatingFilename(containedFile.name)
                if (parser.IdentifiedBy != null && parser.IdentifiedBy.length > 0) {
                    s.setIdentifiedBy(parser.IdentifiedBy)
                }
                log!!.debug(s.Collection)
                s.setCreatedBy(ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.AppVersion)
                s.setDateCreated(Date())
                try { // *** Save a database record of the specimen.
                    specimenLifeCycle.persist(s)
                    counter.incrementSpecimenDatabased()
                    s.attachNewPart()
                } catch (e: SpecimenExistsException) {
                    log.debug(e.message)
                    // Expected case on scanning a second image for a specimen.
// Doesn't need to be reported as a parsing error.
//
// Look up the existing record to link this specimen to it.
                    try {
                        val checkResult: MutableList<Specimen?> = specimenLifeCycle.findByBarcode(barcode)
                        if (checkResult.size == 1) {
                            s = checkResult[0]
                        }
                    } catch (e2: Exception) {
                        s = null // so that saving the image record doesn't fail on trying to save linked transient specimen record.
                        val errorMessage = "Linking Error: \nFailed to link image to existing specimen record.\n"
                        val error = RunnableJobError(image.Filename, barcode,
                                rawBarcode, exifComment, errorMessage,
                                parser, parser as DrawerNameReturner,
                                e2, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                        counter.appendError(error)
                    }
                } catch (e: SaveFailedException) { // Couldn't save for some reason other than the
// specimen record already existing.  Check for possible
// save problems resulting from parsing errors.
                    log.error(e)
                    try {
                        val checkResult: MutableList<Specimen?> = specimenLifeCycle.findByBarcode(barcode)
                        if (checkResult.size == 1) {
                            s = checkResult[0]
                        }
                        // Drawer number with length limit (and specimen that fails to save at over this length makes
// a good canary for labels that parse very badly.
                        if ((parser as DrawerNameReturner).DrawerNumber.length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) {
                            var badParse = ""
                            badParse = "Parsing problem. \nDrawer number is too long: " + s.DrawerNumber + "\n"
                            val error = RunnableJobError(image.Filename, barcode,
                                    rawBarcode, exifComment, badParse,
                                    parser, parser as DrawerNameReturner,
                                    e, RunnableJobError.Companion.TYPE_BAD_PARSE)
                            counter.appendError(error)
                        } else {
                            val error = RunnableJobError(image.Filename, barcode,
                                    rawBarcode, exifComment, e.message,
                                    parser, parser as DrawerNameReturner,
                                    e, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                            counter.appendError(error)
                        }
                    } catch (err: Exception) {
                        log.error(err)
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
                        val error = RunnableJobError(image.Filename, barcode,
                                rawBarcode, exifComment, badParse,
                                parser, parser as DrawerNameReturner,
                                err, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                        counter.appendError(error)
                        counter.incrementFilesFailed()
                        s = null
                    }
                } catch (ex: Exception) {
                    log.error(ex)
                    val error = RunnableJobError(image.Filename, barcode,
                            rawBarcode, exifComment, ex.message,
                            parser, parser as DrawerNameReturner,
                            ex, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                    counter.appendError(error)
                }
            } finally {
                lock.unlock()
            }
            if (s != null) {
                image.setSpecimen(s)
            }
        }

        protected fun setBasicSpecimenFromParser(parser: TaxonNameReturner?, s: Specimen): Specimen {
            if (s.Family.length > 40) {
                s.setFamily(s.Family.substring(0, 40))
            }
            if (parser != null) {
                s.setGenus(parser.Genus)
                s.setSpecificEpithet(parser.SpecificEpithet)
                s.setSubspecificEpithet(parser.SubspecificEpithet)
                s.setInfraspecificEpithet(parser.InfraspecificEpithet)
                s.setInfraspecificRank(parser.InfraspecificRank)
                s.setAuthorship(parser.Authorship)
                s.setDrawerNumber((parser as DrawerNameReturner?).DrawerNumber)
                s.setCollection((parser as CollectionReturner?).Collection)
            }
            return s
        }
    }
}
