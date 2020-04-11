/**
 * JobVerbatimFieldLoad.java
 *
 *
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
package edu.harvard.mcz.imagecapture.loader


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.data.RunStatus
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.Counter
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModel
import edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoad
import edu.harvard.mcz.imagecapture.loader.ex.LoadException
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import java.io.*
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane

/**
 *
 */
class JobVerbatimFieldLoad : RunnableJob, Runnable {
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
    private var listeners: MutableList<RunnerListener?>? = null
    private var counter: Counter? = null
    private var errors: StringBuffer? = null
    private var file: File? = null

    constructor() {
        init()
    }

    constructor(fileToLoad: File?) {
        file = fileToLoad
        init()
    }

    protected fun init() {
        listeners = ArrayList<RunnerListener?>()
        counter = Counter()
        status = RunStatus.STATUS_NEW
        percentComplete = 0
        startTime = null
        errors = StringBuffer()
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    override fun run() {
        log!!.debug(this.toString())
        start()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        Singleton.JobList.addJob(this)
        status = RunStatus.STATUS_RUNNING
        var selectedFilename = ""
        if (file == null) {
            val fileChooser = JFileChooser()
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
            if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH) != null) {
                fileChooser.setCurrentDirectory(File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH)))
            }
            val returnValue: Int = fileChooser.showOpenDialog(Singleton.MainFrame)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.SelectedFile
            }
        }
        if (file != null) {
            log!!.debug("Selected file to load: " + file!!.name + ".")
            if (file!!.exists() && file!!.isFile && file!!.canRead()) { // Save location
                Singleton.Properties.Properties.setProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH, file!!.path)
                selectedFilename = file!!.name
                var headers = arrayOf<String?>()
                var csvFormat = CSVFormat.DEFAULT.withHeader(*headers)
                var rows = 0
                try {
                    rows = readRows(file!!, csvFormat)
                } catch (e: FileNotFoundException) {
                    JOptionPane.showMessageDialog(Singleton.MainFrame, "Unable to load data, file not found: " + e.message, "Error: File Not Found", JOptionPane.OK_OPTION)
                    errors!!.append("File not found ").append(e.message).append("\n")
                    log.error(e.message, e)
                } catch (e: IOException) {
                    errors!!.append("Error loading csv format, trying tab delimited: ").append(e.message).append("\n")
                    log.debug(e.message)
                    try { // try reading as tab delimited format, if successful, use that format.
                        val tabFormat = CSVFormat.newFormat('\t')
                                .withIgnoreSurroundingSpaces(true)
                                .withHeader(*headers)
                                .withQuote('"')
                        rows = readRows(file!!, tabFormat)
                        csvFormat = tabFormat
                    } catch (e1: IOException) {
                        errors!!.append("Error Loading data: ").append(e1.message).append("\n")
                        log.error(e.message, e1)
                    }
                }
                try {
                    val reader: Reader = FileReader(file)
                    val csvParser = CSVParser(reader, csvFormat)
                    val csvHeader = csvParser.headerMap
                    headers = arrayOfNulls<String?>(csvHeader!!.size)
                    var i = 0
                    for (header in csvHeader.keys) {
                        headers[i++] = header
                        log.debug(header)
                    }
                    var okToRun = true
                    //TODO: Work picking/checking responsibility into a FieldLoaderWizard
                    val headerList = Arrays.asList(*headers)
                    if (!headerList.contains("barcode")) {
                        log.error("Input file " + file!!.name + " header does not contain required field 'barcode'.")
                        // no barcode field, we can't match the input to specimen records.
                        errors!!.append("Field \"barcode\" not found in csv file headers.  Unable to load data.").append("\n")
                        okToRun = false
                    }
                    if (okToRun) {
                        val iterator = csvParser.iterator()
                        val fl = FieldLoader()
                        if (headerList.size == 3 && headerList.contains("verbatimUnclassifiedText")
                                && headerList.contains("questions") && headerList.contains("barcode")) {
                            log.debug("Input file matches case 1: Unclassified text only.")
                            // Allowed case 1a: unclassified text only
                            val confirm: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame,
                                    "Confirm load from file $selectedFilename ($rows rows) with just barcode and verbatimUnclassifiedText", "Verbatim unclassified Field found for load", JOptionPane.OK_CANCEL_OPTION)
                            if (confirm == JOptionPane.OK_OPTION) {
                                var barcode = ""
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    counter!!.incrementSpecimenDatabased()
                                    val record = iterator.next()
                                    try {
                                        val verbatimUnclassifiedText = record!!["verbatimUnclassifiedText"]
                                        barcode = record["barcode"]
                                        val questions = record["questions"]
                                        fl.load(barcode, verbatimUnclassifiedText, questions, true)
                                        counter!!.incrementSpecimensUpdated()
                                    } catch (e: IllegalArgumentException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    } catch (e: LoadException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                errors!!.append("Load canceled by user.").append("\n")
                            }
                        } else if (headerList.size == 4 && headerList.contains("verbatimUnclassifiedText")
                                && headerList.contains("questions") && headerList.contains("barcode")
                                && headerList.contains("verbatimClusterIdentifier")) {
                            log.debug("Input file matches case 1: Unclassified text only (with cluster identifier).")
                            // Allowed case 1b: unclassified text only (including cluster identifier)
                            val confirm: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame,
                                    "Confirm load from file $selectedFilename ($rows rows) with just barcode and verbatimUnclassifiedText", "Verbatim unclassified Field found for load", JOptionPane.OK_CANCEL_OPTION)
                            if (confirm == JOptionPane.OK_OPTION) {
                                var barcode = ""
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    counter!!.incrementSpecimenDatabased()
                                    val record = iterator.next()
                                    try {
                                        val verbatimUnclassifiedText = record!!["verbatimUnclassifiedText"]
                                        val verbatimClusterIdentifier = record["verbatimClusterIdentifier"]
                                        barcode = record["barcode"]
                                        val questions = record["questions"]
                                        fl.load(barcode, verbatimUnclassifiedText, verbatimClusterIdentifier, questions, true)
                                        counter!!.incrementSpecimensUpdated()
                                    } catch (e: IllegalArgumentException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    } catch (e: LoadException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                errors!!.append("Load canceled by user.").append("\n")
                            }
                        } else if (headerList.size == 8 && headerList.contains("verbatimUnclassifiedText") && headerList.contains("questions") && headerList.contains("barcode")
                                && headerList.contains("verbatimLocality") && headerList.contains("verbatimDate") && headerList.contains("verbatimNumbers")
                                && headerList.contains("verbatimCollector") && headerList.contains("verbatimCollection")) { // Allowed case two, transcription into verbatim fields, must be exact list of all
// verbatim fields, not including cluster identifier or other metadata.
                            log.debug("Input file matches case 2: Full list of verbatim fields.")
                            val confirm: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame,
                                    "Confirm load from file $selectedFilename ($rows rows) with just barcode and verbatim fields.", "Verbatim Fields found for load", JOptionPane.OK_CANCEL_OPTION)
                            if (confirm == JOptionPane.OK_OPTION) {
                                var barcode = ""
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    counter!!.incrementSpecimenDatabased()
                                    val record = iterator.next()
                                    try {
                                        val verbatimLocality = record!!["verbatimLocality"]
                                        val verbatimDate = record["verbatimDate"]
                                        val verbatimCollector = record["verbatimCollector"]
                                        val verbatimCollection = record["verbatimCollection"]
                                        val verbatimNumbers = record["verbatimNumbers"]
                                        val verbatimUnclasifiedText = record["verbatimUnclassifiedText"]
                                        barcode = record["barcode"]
                                        val questions = record["questions"]
                                        fl.load(barcode, verbatimLocality, verbatimDate, verbatimCollector, verbatimCollection, verbatimNumbers, verbatimUnclasifiedText, questions)
                                        counter!!.incrementSpecimensUpdated()
                                    } catch (e: IllegalArgumentException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    } catch (e: LoadException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                errors!!.append("Load canceled by user.").append("\n")
                            }
                        } else { // allowed case three, transcription into arbitrary sets verbatim or other fields
                            log.debug("Input file case 3: Arbitrary set of fields.")
                            // Check column headers before starting run.
                            var headersOK = false
                            try {
                                val headerCheck: HeaderCheckResult = fl.checkHeaderList(headerList)
                                if (headerCheck.isResult()) {
                                    val confirm: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame,
                                            "Confirm load from file " + selectedFilename + " (" + rows + " rows) with headers: \n" + headerCheck.Message.replace(":".toRegex(), ":\n"), "Fields found for load", JOptionPane.OK_CANCEL_OPTION)
                                    if (confirm == JOptionPane.OK_OPTION) {
                                        headersOK = true
                                    } else {
                                        errors!!.append("Load canceled by user.").append("\n")
                                    }
                                } else {
                                    val confirm: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame,
                                            "Problem found with headers in file, try to load anyway?\nHeaders: \n" + headerCheck.Message.replace(":".toRegex(), ":\n"), "Problem in fields for load", JOptionPane.OK_CANCEL_OPTION)
                                    if (confirm == JOptionPane.OK_OPTION) {
                                        headersOK = true
                                    } else {
                                        errors!!.append("Load canceled by user.").append("\n")
                                    }
                                }
                            } catch (e: LoadException) {
                                errors!!.append("Error loading data: \n").append(e.message).append("\n")
                                JOptionPane.showMessageDialog(Singleton.MainFrame, e.message!!.replace(":".toRegex(), ":\n"), "Error Loading Data: Problem Fields", JOptionPane.ERROR_MESSAGE)
                                log.error(e.message, e)
                            }
                            if (headersOK) {
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    val data: MutableMap<String?, String?> = HashMap()
                                    val record = iterator.next()
                                    val barcode = record!!["barcode"]
                                    val hi = headerList.iterator()
                                    var containsNonVerbatim = false
                                    while (hi.hasNext()) {
                                        val header = hi.next()
                                        // Skip any fields prefixed by the underscore character _
                                        if (header != "barcode" && !header!!.startsWith("_")) {
                                            data[header] = record[header]
                                            if (header != "questions" && MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, header) && !MetadataRetriever.isFieldVerbatim(Specimen::class.java, header)) {
                                                containsNonVerbatim = true
                                            }
                                        }
                                    }
                                    if (data.size > 0) {
                                        try {
                                            var updated = false
                                            updated = if (containsNonVerbatim) {
                                                fl.loadFromMap(barcode, data, WorkFlowStatus.STAGE_CLASSIFIED, true)
                                            } else {
                                                fl.loadFromMap(barcode, data, WorkFlowStatus.STAGE_VERBATIM, true)
                                            }
                                            counter!!.incrementSpecimenDatabased()
                                            if (updated) {
                                                counter!!.incrementSpecimensUpdated()
                                            }
                                        } catch (e1: HibernateException) { // Catch (should just be development) problems with the underlying query
                                            val message = StringBuilder()
                                            message.append("Query Error loading row (").append(lineNumber).append(")[").append(barcode).append("]").append(e1.message)
                                            val err = RunnableJobError(selectedFilename, barcode, Integer.toString(lineNumber), e1.message, e1, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                            counter!!.appendError(err)
                                            log.error(e1.message, e1)
                                        } catch (e: LoadException) {
                                            val message = StringBuilder()
                                            message.append("Error loading row (").append(lineNumber).append(")[").append(barcode).append("]").append(e.message)
                                            val err = RunnableJobError(selectedFilename, barcode, Integer.toString(lineNumber), e.message, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                            counter!!.appendError(err)
                                            // errors.append(message.append("\n").toString());
                                            log.error(e.message, e)
                                        }
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                val message = "Can't load data, problem with headers."
                                errors!!.append(message).append("\n")
                                log.error(message)
                            }
                        }
                    }
                    csvParser.close()
                    reader.close()
                } catch (e: FileNotFoundException) {
                    JOptionPane.showMessageDialog(Singleton.MainFrame, "Unable to load data, file not found: " + e.message, "Error: File Not Found", JOptionPane.OK_OPTION)
                    errors!!.append("File not found ").append(e.message).append("\n")
                    log.error(e.message, e)
                } catch (e: IOException) {
                    errors!!.append("Error Loading data: ").append(e.message).append("\n")
                    log.error(e.message, e)
                }
            }
        } else { //TODO: handle error condition
            log!!.error("File selection cancelled by user.")
        }
        report(selectedFilename)
        done()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
     */
    override fun stop(): Boolean { // TODO Auto-generated method stub
        return false
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        log!!.debug(this.javaClass.simpleName + " " + this.toString() + "  recieved cancel signal")
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
    override fun registerListener(aJobListener: RunnerListener?): Boolean {
        return listeners!!.add(aJobListener)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = "Ingest Data obtained from an external process"

    /**
     * Cleanup when job is complete.
     */
    protected fun done() {
        status = RunStatus.STATUS_DONE
        notifyListeners(RunStatus.STATUS_DONE)
        Singleton.JobList.removeJob(this)
    }

    private fun report(selectedFilename: String?) {
        var report = "Results for loading data from file $selectedFilename.\n"
        report += "Found  " + counter!!.specimens + " rows in input file.\n"
        report += "Examined " + counter!!.specimens + " specimens.\n"
        report += "Saved updated values for " + counter!!.specimensUpdated + " specimens.\n"
        report += errors.toString()
        Singleton.MainFrame.setStatusMessage("Load data from file complete.")
        val errorReportDialog = RunnableJobReportDialog(
                Singleton.MainFrame,
                report, counter!!.errors,
                RunnableJobErrorTableModel.Companion.TYPE_LOAD,
                "Load Data from file Report"
        )
        errorReportDialog.setVisible(true)
    }

    protected fun notifyListeners(anEvent: Int) {
        Singleton.MainFrame.notifyListener(anEvent, this)
        val i: MutableIterator<RunnerListener?> = listeners!!.iterator()
        while (i.hasNext()) {
            i.next().notifyListener(anEvent, this)
        }
    }

    protected fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        log!!.debug(percentComplete)
        //notify listeners
        notifyListeners(percentComplete)
    }

    /**
     * Attempt to read file with a given CSV format, and if successful, return
     * the number of rows in the file.
     *
     * @param file        to check for csv rows.
     * @param formatToTry the CSV format to try to read the file with.
     * @return number of rows in the file.
     * @throws IOException           on a problem reading the header.
     * @throws FileNotFoundException on not finding the file.
     */
    @Throws(IOException::class, FileNotFoundException::class)
    protected fun readRows(file: File, formatToTry: CSVFormat): Int {
        var rows = 0
        val reader: Reader = FileReader(file)
        val csvParser = CSVParser(reader, formatToTry)
        val iterator = csvParser.iterator()
        while (iterator.hasNext()) {
            iterator.next()
            rows++
        }
        csvParser.close()
        reader.close()
        return rows
    }

    companion object {
        private val log = LogFactory.getLog(JobVerbatimFieldLoad::class.java)
    }
}
