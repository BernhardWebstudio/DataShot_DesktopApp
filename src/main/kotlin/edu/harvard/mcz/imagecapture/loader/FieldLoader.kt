/**
 * FieldLoader.java
 * edu.harvard.mcz.imagecapture.loader
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


import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.entity.ExternalHistory
import edu.harvard.mcz.imagecapture.entity.Number
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.loader.FieldLoader
import edu.harvard.mcz.imagecapture.loader.ex.*
import org.apache.commons.logging.LogFactory
import org.filteredpush.qc.date.DateUtils
import org.filteredpush.qc.date.EventResult
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 *
 */
class FieldLoader {
    protected var sls: SpecimenLifeCycle? = null
    protected var knownFields: MutableMap<String?, String?>? = null
    /**
     * Setup initial conditions, construct list of known fields into which data can be put.
     */
    protected fun init() {
        sls = SpecimenLifeCycle()
        // Key: lower case of field, Value actual case of Field.
        knownFields = HashMap()
        val specimenMethods: Array<Method?> = Specimen::class.java.getDeclaredMethods()
        for (j in specimenMethods.indices) {
            if (specimenMethods[j]!!.name.startsWith("set") && specimenMethods[j]!!.parameterTypes.size == 1 && specimenMethods[j]!!.parameterTypes[0].name == String::class.java.name) {
                knownFields[specimenMethods[j]!!.name.replace("^set".toRegex(), "").toLowerCase()] = specimenMethods[j]!!.name.replace("^set".toRegex(), "")
            }
        }
        // List of input fields that will need to be parsed into relational tables
        val toParseFields = ArrayList<String?>()
        toParseFields.add("collectors")
        toParseFields.add("numbers")
        val ipf = toParseFields.iterator()
        while (ipf.hasNext()) {
            val parseField = ipf.next()
            knownFields[parseField!!.toLowerCase()] = parseField
        }
        if (log!!.isDebugEnabled) {
            val i = knownFields.keys.iterator()
            while (i.hasNext()) {
                log.debug(i.next())
            }
        }
    }

    /**
     * Check whether or not a header is in the list of known fields.
     *
     * @param possibleField header to check
     * @return true if possibleField is (case insensitive) in the list of known
     * fields, false if not.  Throws a null pointer exception if possibleField is null.
     */
    fun isFieldKnown(possibleField: String): Boolean {
        return knownFields!!.containsKey(possibleField.toLowerCase())
    }

    @Throws(LoadException::class)
    fun load(barcode: String?, verbatimUnclassifiedText: String?, questions: String?, overwriteExisting: Boolean): Boolean {
        return this.load(barcode, verbatimUnclassifiedText, null, questions, overwriteExisting)
    }

    /**
     * Given a barcode number and a value for verbatimUnclassifiedText, update the verbatim value for the matching
     * Specimen record, leaves the Specimen record in workflow state WorkFlowStatus.STAGE_VERBATIM.
     *
     * @param barcode                  must match exactly one Specimen record.
     * @param verbatimUnclassifiedText value for this field in Specimen.
     * @param questions                value to append to this field in Specimen.
     * @param overwriteExisting        if true, overwrite any value of verbatimUnclassifiedText in the matching Specimen record.
     * @return if the new value was saved
     * @throws LoadException on an error
     */
    @Throws(LoadException::class)
    fun load(barcode: String?, verbatimUnclassifiedText: String?, verbatimClusterIdentifier: String?, questions: String?, overwriteExisting: Boolean): Boolean {
        var result = false
        val matches: MutableList<Specimen?> = sls.findByBarcode(barcode)
        if (matches != null && matches.size == 1) {
            val match: Specimen? = matches[0]
            if (!overwriteExisting && !WorkFlowStatus.allowsVerbatimUpdate(match.getWorkFlowStatus())
                    ||
                    overwriteExisting && !WorkFlowStatus.allowsVerbatimUpdateOverwrite(match.getWorkFlowStatus())) {
                throw LoadTargetMovedOnException()
            } else {
                if (match.getVerbatimUnclassifiedText() == null || match.getVerbatimUnclassifiedText().trim({ it <= ' ' }).length == 0 || overwriteExisting) {
                    match.setVerbatimUnclassifiedText(verbatimUnclassifiedText)
                } else {
                    throw LoadTargetPopulatedException()
                }
                match.setVerbatimClusterIdentifier(verbatimClusterIdentifier)
                // append any questions to current questions.
                if (questions != null && questions.trim { it <= ' ' }.length > 0) {
                    var currentQuestions: String = match.getQuestions()
                    if (currentQuestions == null) {
                        currentQuestions = ""
                    }
                    if (currentQuestions.trim { it <= ' ' }.length > 0) {
                        currentQuestions = "$currentQuestions | "
                    }
                    match.setQuestions(currentQuestions + questions)
                }
                match.setWorkFlowStatus(WorkFlowStatus.STAGE_VERBATIM)
                try {
                    sls.attachDirty(match)
                    result = true
                    logHistory(match, "Load:" + WorkFlowStatus.STAGE_VERBATIM + ":VerbatimUnclassifiedText", Date())
                } catch (e: SaveFailedException) {
                    log!!.error(e.message, e)
                    throw LoadTargetSaveException("Error saving updated target record: " + e.message)
                }
            }
        } else {
            throw LoadTargetRecordNotFoundException()
        }
        return result
    }

    /**
     * Give a barcode number and the set of verbatim fields, attempt to set the values for those verbatim fields for a record.
     * Does not overwrite existing non-empty values, does not modify record if any verbatim field contains a value.
     *
     * @param barcode                  field, must match on exactly one Specimen record.
     * @param verbatimLocality         value for this field in Specimen.
     * @param verbatimDate             value for this field in Specimen.
     * @param verbatimCollector        value for this field in Specimen.
     * @param verbatimCollection       value for this field in Specimen.
     * @param verbatimNumbers          value for this field in Specimen.
     * @param verbatimUnclassifiedText value for this field in Specimen.
     * @param questions                value to append to this field in Specimen.
     * @return true if record with the provided barcode number was updated.
     * @throws LoadException on an error, including any existing value for any of the verbatim fields.
     */
    @Throws(LoadException::class)
    fun load(barcode: String?, verbatimLocality: String?, verbatimDate: String?, verbatimCollector: String?, verbatimCollection: String?, verbatimNumbers: String?, verbatimUnclassifiedText: String?, questions: String?): Boolean {
        val result = false
        val matches: MutableList<Specimen?> = sls.findByBarcode(barcode)
        if (matches != null && matches.size == 1) {
            val match: Specimen? = matches[0]
            if (WorkFlowStatus.allowsVerbatimUpdate(match.getWorkFlowStatus())) {
                if (match.getVerbatimLocality() == null || match.getVerbatimLocality().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimLocality(verbatimLocality)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getDateNos() == null || match.getDateNos().trim({ it <= ' ' }).length == 0) {
                    match.setDateNos(verbatimDate)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimCollector() == null || match.getVerbatimCollector().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimCollector(verbatimCollector)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimCollection() == null || match.getVerbatimCollection().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimCollection(verbatimCollection)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimNumbers() == null || match.getVerbatimNumbers().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimNumbers(verbatimNumbers)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimUnclassifiedText() == null || match.getVerbatimUnclassifiedText().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimUnclassifiedText(verbatimUnclassifiedText)
                } else {
                    throw LoadTargetPopulatedException()
                }
                // append any questions to current questions.
                if (questions != null && questions.trim { it <= ' ' }.length > 0) {
                    var currentQuestions: String = match.getQuestions()
                    if (currentQuestions == null) {
                        currentQuestions = ""
                    }
                    if (currentQuestions.trim { it <= ' ' }.length > 0) {
                        currentQuestions = "$currentQuestions | "
                    }
                    match.setQuestions(currentQuestions + questions)
                }
                match.setWorkFlowStatus(WorkFlowStatus.STAGE_VERBATIM)
                try {
                    sls.attachDirty(match)
                    logHistory(match, "VerbatimFields:" + WorkFlowStatus.STAGE_VERBATIM + ":", Date())
                } catch (e: SaveFailedException) {
                    log!!.error(e.message, e)
                    throw LoadTargetSaveException("Error saving updated target record: " + e.message)
                }
            } else {
                throw LoadTargetMovedOnException()
            }
        } else {
            throw LoadTargetRecordNotFoundException()
        }
        return result
    }

    /**
     * Give a barcode number and an arbitrary set of fields in Specimen, attempt to set the values for those fields for a record.
     *
     * @param barcode                     field, must match on exactly one Specimen record.
     * @param data                        map of field names and data values
     * @param questions                   value to append to this field in Specimen.
     * @param newWorkflowStatus           to set Specimen.workflowStatus to.
     * @param allowUpdateExistingVerbatim if true can load can overwrite the value in an existing verbatim field.
     * @return true if one or more fields were updated.
     * @throws LoadException on an error (particularly from inability to map keys in data to fields in Specimen.
     */
    @Throws(LoadException::class)
    fun loadFromMap(barcode: String?, data: MutableMap<String?, String?>, newWorkflowStatus: String?, allowUpdateExistingVerbatim: Boolean): Boolean {
        var result = false
        log!!.debug(barcode)
        val knownFields = ArrayList<String?>()
        val knownFieldsLowerUpper = HashMap<String?, String?>()
        val specimenMethods: Array<Method?> = Specimen::class.java.getDeclaredMethods()
        for (j in specimenMethods.indices) {
            if (specimenMethods[j]!!.name.startsWith("set") && specimenMethods[j]!!.parameterTypes.size == 1 && specimenMethods[j]!!.parameterTypes[0].name == String::class.java.name) {
                val actualCase = specimenMethods[j]!!.name.replace("^set".toRegex(), "")
                knownFields.add(specimenMethods[j]!!.name.replace("^set".toRegex(), ""))
                knownFieldsLowerUpper[actualCase.toLowerCase()] = actualCase
            }
        }
        // List of input fields that will need to be parsed into relational tables
        val toParseFields = ArrayList<String?>()
        toParseFields.add("collectors")
        toParseFields.add("numbers")
        // Check that the proposed new state is allowed.
        if (newWorkflowStatus == null ||
                newWorkflowStatus != WorkFlowStatus.STAGE_VERBATIM &&
                newWorkflowStatus != WorkFlowStatus.STAGE_CLASSIFIED) {
            throw LoadException("Trying to load into unallowed new state.$newWorkflowStatus")
        }
        // Retrieve existing record for update (thus not blanking existing fields, and allowing for not updating fields with values, or appending comments).
        val matches: MutableList<Specimen?> = sls.findByBarcode(barcode)
        if (matches != null && matches.size == 1) {
            val match: Specimen? = matches[0]
            if (newWorkflowStatus == WorkFlowStatus.STAGE_VERBATIM && !WorkFlowStatus.allowsVerbatimUpdate(match.getWorkFlowStatus())
                    ||
                    newWorkflowStatus == WorkFlowStatus.STAGE_CLASSIFIED && !WorkFlowStatus.allowsClassifiedUpdate(match.getWorkFlowStatus())) { // The target Specimen record has moved on past the state where it can be altered by a data load.
                throw LoadTargetMovedOnException(barcode + " is in state " + match.getWorkFlowStatus() + " and can't be altered by this data load (to " + newWorkflowStatus + ").")
            } else { // Target Specimen record is eligible to be updated by a data load.
                var foundData = false
                var hasChange = false
                var hasExternalWorkflowProcess = false
                var hasExternalWorkflowDate = false
                val i = data.keys.iterator()
                var separator = ""
                val keys = StringBuilder()
                while (i.hasNext()) {
                    val keyOrig = i.next()
                    val key = keyOrig!!.toLowerCase()
                    val actualCase = knownFieldsLowerUpper[key]
                    if (toParseFields.contains(key) ||
                            actualCase != null && knownFields.contains(actualCase) && key != "barcode" && MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, key)) {
                        var datavalue = data[keyOrig]
                        keys.append(separator).append(key)
                        separator = ","
                        var setMethod: Method
                        try {
                            if (key == "collectors") { // Special case, parse collectors to associated Collector table.
                                datavalue = "$datavalue|"
                                val collectors: Array<String?> = datavalue.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                log.debug(collectors.size)
                                for (j in collectors.indices) {
                                    val collector = collectors[j]
                                    log.debug(collector)
                                    if (collector!!.trim { it <= ' ' }.length > 0) { // Check to see if Collector exists
                                        val existingCollectors: MutableSet<Collector?> = match.getCollectors()
                                        val ic = existingCollectors.iterator()
                                        var exists = false
                                        while (ic.hasNext()) {
                                            val c = ic.next()
                                            if (c!!.collectorName == collector) {
                                                exists = true
                                            }
                                        }
                                        if (!exists) { // only add if it isn't allready present.
                                            val col = Collector()
                                            col.specimen = match
                                            col.collectorName = collector
                                            val cls = CollectorLifeCycle()
                                            cls.persist(col)
                                            match.getCollectors().add(col)
                                            foundData = true
                                            hasChange = true
                                        }
                                    }
                                }
                            } else if (key.toLowerCase() == "numbers") { // Special case, parse numbers to associated Number table.
                                datavalue = "$datavalue|"
                                val numbers: Array<String?> = datavalue.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (j in numbers.indices) {
                                    val numberKV = numbers[j]
                                    if (numberKV!!.trim { it <= ' ' }.length > 0) {
                                        var number = numberKV
                                        var numType = "unknown"
                                        if (numberKV.contains(":")) {
                                            val numbits: Array<String?> = numberKV.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                            number = numbits[0]
                                            numType = numbits[1]
                                            if (numType == null || numType.trim { it <= ' ' }.length == 0) {
                                                numType = "unknown"
                                            }
                                        }
                                        // check to see if number exists
                                        val existingNumbers: MutableSet<Number?> = match.getNumbers()
                                        val ic = existingNumbers.iterator()
                                        var exists = false
                                        while (ic.hasNext()) {
                                            val c = ic.next()
                                            if (c!!.number == number || c.numberType == numType) {
                                                exists = true
                                            }
                                        }
                                        if (!exists) {
                                            val nls = NumberLifeCycle()
                                            // only add if it isn't already present.
                                            val num = Number()
                                            num.number = number
                                            num.numberType = numType
                                            num.specimen = match
                                            nls.persist(num)
                                            hasChange = true
                                            match.getNumbers().add(num)
                                            foundData = true
                                        }
                                    }
                                }
                            } else { // Find the Specimen get and set methods for the current key
                                setMethod = Specimen::class.java.getMethod("set$actualCase", String::class.java)
                                val getMethod: Method = Specimen::class.java.getMethod("get$actualCase", null)
                                // Obtain the current value in the Specimen record for the field matching the current key.
                                val currentValue = (getMethod.invoke(match, null) as String)
                                // Assess whether changes to existing data are allowed for that field,
// make changes only if they are allowed.
                                if (key == "externalworkflowprocess") {
                                    hasExternalWorkflowProcess = true
                                }
                                if (key == "externalworkflowdate") {
                                    hasExternalWorkflowDate = true
                                }
                                if (key == "questions") { // append
                                    if (currentValue != null && currentValue.trim { it <= ' ' }.length > 0) {
                                        datavalue = "$currentValue | $datavalue"
                                    }
                                    setMethod.invoke(match, datavalue)
                                    foundData = true
                                    hasChange = true
                                } else if (key == "externalworkflowprocess" || key == "externalworkflowdate" || key == "verbatimclusteridentifier") { // overwrite existing metadata
                                    setMethod.invoke(match, datavalue)
                                    foundData = true
                                    hasChange = true
                                } else {
                                    if (currentValue == null || currentValue.trim { it <= ' ' }.length == 0) { // Handle ISO date formatting variants
                                        if (key.equals("ISODate", ignoreCase = true)) {
                                            val parseResult: EventResult? = DateUtils.extractDateFromVerbatimER(datavalue)
                                            if (parseResult.getResultState() == EventResult.EventQCResultState.DATE || parseResult.getResultState() == EventResult.EventQCResultState.RANGE) {
                                                val correctISOFormat: String = parseResult.getResult()
                                                // switch from correct ISO format to the internally stored incorrect format that switches - and /.
                                                datavalue = correctISOFormat.replace("/", "^") // change / to placeholder
                                                datavalue = datavalue.replace("-", "/") // change - to /
                                                datavalue = datavalue.replace("^", "-") // change placeholder to -
                                            }
                                        }
                                        // overwrite verbatim fields if update is allowed, otherwise no overwite of existing data.
                                        log.debug("Set: $actualCase = $datavalue")
                                        setMethod.invoke(match, datavalue)
                                        foundData = true
                                    } else if (MetadataRetriever.isFieldVerbatim(Specimen::class.java, key) && allowUpdateExistingVerbatim) {
                                        setMethod.invoke(match, datavalue)
                                        foundData = true
                                        hasChange = true
                                    } else {
                                        log.error("Skipped set$actualCase = $datavalue")
                                    }
                                }
                            }
                        } catch (e: NoSuchMethodException) {
                            throw LoadException(e.message, e)
                        } catch (e: SaveFailedException) {
                            throw LoadException(e.message, e)
                        } catch (e: SecurityException) {
                            log.error(e.message, e)
                            throw LoadException(e.message)
                        } catch (e: IllegalAccessException) {
                            log.error(e.message, e)
                            throw LoadException(e.message)
                        } catch (e: IllegalArgumentException) {
                            log.error(e.message, e)
                            throw LoadException(e.message)
                        } catch (e: InvocationTargetException) {
                            log.error(e.message, e)
                            throw LoadException(e.message, e)
                        }
                    } else {
                        log.error("Key: $key")
                        log.error("Key (actual case of method): $actualCase")
                        log.error("knownFields.contains(actualCase): " + knownFields.contains(actualCase))
                        log.error("toParseFields.contains(key): " + toParseFields.contains(key))
                        log.error("isFieldExternallyUpdatable:" + MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, key))
                        throw LoadException("Column $key is not an externally updateable field of Specimen.")
                    }
                }
                if (foundData) {
                    try { // save the updated specimen record
                        match.setWorkFlowStatus(newWorkflowStatus)
                        // with the user running the load job and the current date as last update.
                        match.setLastUpdatedBy(Singleton.getUserFullName())
                        match.setDateLastUpdated(Date())
                        log.debug("Updating:" + match.getBarcode())
                        sls.attachDirty(match)
                        result = hasChange
                        // If we were provided
                        var ewProcess = "ArbitraryFieldLoad:" + match.getWorkFlowStatus() + ":" + keys.toString()
                        if (hasExternalWorkflowProcess) {
                            ewProcess = match.getExternalWorkflowProcess()
                        }
                        var ewDate = Date()
                        if (hasExternalWorkflowDate) {
                            ewDate = match.getExternalWorkflowDate()
                        }
                        logHistory(match, ewProcess, ewDate)
                    } catch (e: SaveFailedException) {
                        log.error(e.message, e)
                        throw LoadTargetSaveException()
                    }
                }
            }
        }
        return result
    }

    /**
     * Test the headers of a file for conformance with the expectations of loadFromMap
     *
     * @param headers the headers to check against allowed fields in List<String> form.
     * @return a HeaderCheckResult object containing a result (true) and a message, the
     * result is true if there are no unmatched fields in the load, currently exceptions
     * are thrown instead of any false cases for result.
     * @throws LoadException if no barcode field is found, if no data fields are found, or if
     * one or more unknown (not mapped to DataShot specimen) fields are found.
     * @see HeaderCheckResult.loadFromMap
    </String> */
    @Throws(LoadException::class)
    fun checkHeaderList(headers: MutableList<String?>): HeaderCheckResult {
        val result = HeaderCheckResult()
        val knownFields = ArrayList<String?>()
        val knownFieldsLowerUpper = HashMap<String?, String?>()
        val specimenMethods: Array<Method?> = Specimen::class.java.getDeclaredMethods()
        for (j in specimenMethods.indices) {
            if (specimenMethods[j]!!.name.startsWith("set") && specimenMethods[j]!!.parameterTypes.size == 1 && specimenMethods[j]!!.parameterTypes[0].name == String::class.java.name) {
                val actualCase = specimenMethods[j]!!.name.replace("^set".toRegex(), "")
                knownFields.add(specimenMethods[j]!!.name.replace("^set".toRegex(), ""))
                knownFieldsLowerUpper[actualCase.toLowerCase()] = actualCase
                log!!.debug(actualCase)
            }
        }
        // List of input fields that will need to be parsed into relational tables
        val toParseFields = ArrayList<String?>()
        toParseFields.add("collectors")
        toParseFields.add("numbers")
        val i = headers.iterator()
        var containsBarcode = false
        var containsAField = false
        var containsUnknownField = false
        while (i.hasNext()) {
            val keyOrig = i.next()
            val key = keyOrig!!.toLowerCase()
            if (key == "barcode") {
                containsBarcode = true
                result.addToMessage(keyOrig)
            } else {
                if (key.startsWith("_")) {
                    result.addToMessage("[$keyOrig=Skipped]")
                } else {
                    val actualCase = knownFieldsLowerUpper[key]
                    if (toParseFields.contains(key) ||
                            actualCase != null && knownFields.contains(actualCase) && MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, key)) {
                        result.addToMessage(keyOrig)
                        containsAField = true
                    } else {
                        result.addToMessage("[$keyOrig=Unknown]")
                        containsUnknownField = true
                    }
                }
            }
        }
        if (!containsBarcode) {
            throw LoadException("Header does not contain a barcode field.  \nFields:" + result.getMessage())
        }
        if (!containsAField) {
            throw LoadException("Header contains no recognized data fields. \nFields: " + result.getMessage())
        }
        if (containsUnknownField) {
            throw LoadException("Header contains at least one unknown field. \nFields: " + result.getMessage())
        }
        result.setResult(true)
        return result
    }

    protected fun logHistory(match: Specimen?, externalWorkflowProcess: String?, externalWorkflowDate: Date?) {
        try { // log the external data import
            val history = ExternalHistory()
            history.setExternalWorkflowProcess(externalWorkflowProcess)
            history.setExternalWorkflowDate(externalWorkflowDate)
            history.setSpecimen(match)
            val els = ExternalHistoryLifeCycle()
            els.attachDirty(history)
        } catch (ex: SaveFailedException) {
            log!!.error(ex.message, ex)
        }
    }

    companion object {
        private val log = LogFactory.getLog(FieldLoader::class.java)
    }

    init {
        init()
    }
}
