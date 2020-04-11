/**
 * RunnableJobError.java
 * edu.harvard.mcz.imagecapture.data
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


import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner

/**
 * RunnableJobError, a class for logging the details of multiple sorts of problems
 * that can be found in preproccessing an image.  Allows a report of preprocessing errors to be
 * displayed in a table.  Handles subtypes of errors with the TYPE_ constants.
 */
class RunnableJobError {
    /**
     * @return the filename
     */
    /**
     * @param filename the filename to set
     */
    var filename: String?
    /**
     * @return the barcode
     */
    /**
     * @param barcode the barcode to set
     */
    var barcode: String?
    /**
     * @return the qrBarcode
     */
    /**
     * @param qrBarcode the qrBarcode to set
     */
    var qrBarcode: String?
    /**
     * @return the commentBarcode
     */
    /**
     * @param commentBarcode the commentBarcode to set
     */
    var commentBarcode: String?
    /**
     * @return the errorMessage
     */
    /**
     * @param errorMessage the errorMessage to set
     */
    var errorMessage: String?
    private var taxonParser: TaxonNameReturner?
    private var drawerParser: DrawerNameReturner?
    /**
     * @return the exception
     */
    /**
     * @param exception the exception to set
     */
    var exception: Exception?
    private var failureType: Int
    var previous: String? = null
    var previousPath: String? = null
    /**
     * The line number: used for failure of the VerbatimFieldLoad Job to know which field/line it failed on
     */
    var lineNumber: String?
        private set

    /**
     * Constructor for errors in the expected form for Barcode number QC Checks.
     *
     * @param filename
     * @param barcode
     * @param qrBarcode
     * @param commentBarcode
     * @param errorMessage
     * @param taxonParser
     * @param drawerParser
     * @param exception
     * @param previous
     */
    constructor(filename: String?, barcode: String?,
                qrBarcode: String?, commentBarcode: String?, errorMessage: String?,
                taxonParser: TaxonNameReturner?, drawerParser: DrawerNameReturner?,
                exception: Exception?, failureType: Int, previous: String?, aPreviousPath: String?) {
        this.filename = filename
        this.barcode = barcode
        this.qrBarcode = qrBarcode
        this.commentBarcode = commentBarcode
        this.errorMessage = errorMessage
        this.taxonParser = taxonParser
        this.drawerParser = drawerParser
        this.exception = exception
        this.failureType = failureType
        this.previous = previous
        previousPath = aPreviousPath
        lineNumber = ""
    }

    /**
     * Constructor for errors in the expected form for preprocessing images and repeating OCR.
     *
     * @param filename
     * @param barcode
     * @param qrBarcode
     * @param commentBarcode
     * @param errorMessage
     * @param taxonParser
     * @param drawerParser
     * @param exception
     * @param failureType
     */
    constructor(filename: String?, barcode: String?,
                qrBarcode: String?, commentBarcode: String?, errorMessage: String?,
                taxonParser: TaxonNameReturner?, drawerParser: DrawerNameReturner?,
                exception: Exception?, failureType: Int) {
        this.filename = filename
        this.barcode = barcode
        this.qrBarcode = qrBarcode
        this.commentBarcode = commentBarcode
        this.errorMessage = errorMessage
        this.taxonParser = taxonParser
        this.drawerParser = drawerParser
        this.exception = exception
        this.failureType = failureType
        previous = ""
        lineNumber = ""
    }

    /**
     * Constructor for errors in the expected form for data loading errors.
     *
     * @param filename
     * @param barcode
     * @param lineNumber
     * @param errorMessage
     * @param exception
     * @param failureType
     */
    constructor(filename: String?, barcode: String?,
                lineNumber: String?, errorMessage: String?,
                exception: Exception?, failureType: Int) {
        this.filename = filename
        this.barcode = barcode
        qrBarcode = ""
        commentBarcode = ""
        this.errorMessage = errorMessage
        taxonParser = null
        drawerParser = null
        this.exception = exception
        this.failureType = failureType
        previous = ""
        this.lineNumber = lineNumber
    }

    /**
     * Constructor for errors in the expected form for data loading errors.
     *
     * @param filename
     * @param barcode
     * @param errorMessage
     * @param exception
     * @param failureType
     */
    constructor(filename: String?, barcode: String?, errorMessage: String?,
                exception: Exception?, failureType: Int) {
        this.filename = filename
        this.barcode = barcode
        qrBarcode = ""
        commentBarcode = ""
        this.errorMessage = errorMessage
        taxonParser = null
        drawerParser = null
        this.exception = exception
        this.failureType = failureType
        previous = ""
        lineNumber = ""
    }

    fun asString(): String {
        var result = ""
        var exceptionMessage: String? = ""
        if (exception != null) {
            exceptionMessage = exception!!.message
        }
        result = when (failureType) {
            TYPE_SAVE_FAILED -> getFailureType() + ":" + filename + " " + barcode + "\n" + errorMessage + "\n" + exceptionMessage
            TYPE_MISMATCH -> getFailureType() + ":" + filename + " " + qrBarcode + " != " + commentBarcode
            TYPE_NO_TEMPLATE -> getFailureType() + ":" + filename + " " + qrBarcode + ", " + commentBarcode
            TYPE_BARCODE_MISSING_FROM_SEQUENCE -> getFailureType() + ":" + barcode
            else -> getFailureType() + ": " + exceptionMessage
        }
        return result
    }

    /**
     * @return the taxonParser
     */
    fun getTaxonParser(): TaxonNameReturner? {
        return taxonParser
    }

    /**
     * @param taxonParser the taxonParser to set
     */
    fun setTaxonParser(taxonParser: TaxonNameReturner?) {
        this.taxonParser = taxonParser
    }

    /**
     * @return the drawerParser
     */
    fun getDrawerParser(): DrawerNameReturner? {
        return drawerParser
    }

    /**
     * @param drawerParser the drawerParser to set
     */
    fun setDrawerParser(drawerParser: DrawerNameReturner?) {
        this.drawerParser = drawerParser
    }

    fun getFailureType(): String {
        var result = ""
        result = when (failureType) {
            TYPE_SAVE_FAILED -> "Save Failed"
            TYPE_MISMATCH -> "Barcode Mismatch"
            TYPE_NO_TEMPLATE -> "No Template"
            TYPE_BARCODE_MISSING_FROM_SEQUENCE -> "Not Found"
            TYPE_BAD_PARSE -> "Parsing problem"
            TYPE_DUPLICATE -> "Duplicate image record"
            TYPE_FAILOVER_TO_OCR -> "Failover to OCR"
            TYPE_LOAD_FAILED -> "Data Load Failed"
            TYPE_LOAD_NOCHANGE -> "Data Not Changed"
            TYPE_FILE_READ -> "Data not read"
            else -> "Unhandled case"
        }
        return result
    }

    override fun toString(): String {
        return asString()
    }

    companion object {
        const val TYPE_SAVE_FAILED = 0
        const val TYPE_NO_TEMPLATE = 1
        const val TYPE_MISMATCH = 2
        const val TYPE_BARCODE_MISSING_FROM_SEQUENCE = 3
        const val TYPE_BAD_PARSE = 4
        const val TYPE_DUPLICATE = 5
        const val TYPE_FAILOVER_TO_OCR = 6
        const val TYPE_LOAD_FAILED = 7
        const val TYPE_LOAD_NOCHANGE = 8
        const val TYPE_FILE_READ = 9
        const val TYPE_UNKNOWN = 10
    }
}
