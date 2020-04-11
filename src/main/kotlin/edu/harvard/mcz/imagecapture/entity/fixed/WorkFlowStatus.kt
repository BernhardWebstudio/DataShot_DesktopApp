/**
 * WorkFlowStatus.java
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
package edu.harvard.mcz.imagecapture.entity.fixed


/**
 * Authority list for values of Specimen.workFlowStatus.
 */
object WorkFlowStatus {
    /**
     * Specimen record created from barcode, with current determination, higher
     * taxon, and drawer number populated by OCR and parsing of unit tray label.
     */
    val STAGE_0: String? = "OCR"
    /**
     * Specimen record with clean with current determination, higher taxon, and
     * drawer number, either after human cleanup of OCR, or when record was
     * populated from taxon label data encoded in a barcode on the unit tray
     * label.
     */
    val STAGE_1: String? = "Taxon Entered"
    /**
     * Specimen record where the text from the pin labels has been transcribed
     * into verbatim database fields by a data entry person (or in an external
     * transcription process).  Data transcribed into verbatim fields, but still
     * needs to be interpreted further into atomic fields.
     */
    val STAGE_VERBATIM: String? = "Verbatim Entered"
    /**
     * Specimen record where the text from the pin labels has been transcribed
     * into verbatim database fields by a data entry person (or in an external
     * transcription process) and this verbatim data has been further interpreted
     * into atomic fields, but all data may not yet have been entered from the
     * image.
     */
    val STAGE_CLASSIFIED: String? = "Verbatim Classified"
    /**
     * Specimen record where the text from the pin labels has been transcribed
     * into database fields by a data entry person.
     */
    val STAGE_2: String? = "Text Entered"
    /**
     * State change for a specimen record where a human or an automated process
     * has identified a quality control issue with the record.
     */
    val STAGE_QC_FAIL: String? = "QC Problems"
    /**
     * State change for a specimen record on review after text entry by a person
     * other than the data entry person who did the text entry.
     */
    val STAGE_QC_PASS: String? = "QC Reviewed"
    /**
     * State change for a specimen record indicating that the specimen record that
     * has been reviewed by a taxonomist.
     */
    val STAGE_CLEAN: String? = "Specialist Reviewed"
    /**
     * Specimen record has moved at least into the MCZbase bulkloader.  Flags
     * indicate further progress to complete load of all data into MCZbase. Record
     * is now not editable in DataShot.
     */
    val STAGE_DONE: String? = "Moved to MCZbase"
    /**
     * Specimen record has to be deleted.
     */
    val STAGE_DELETE: String? = "Removal Requested"
    private val CHANGABLE_VALUES: Array<String?>? = arrayOf(
            STAGE_0, STAGE_1, STAGE_VERBATIM, STAGE_CLASSIFIED, STAGE_2,
            STAGE_QC_FAIL, STAGE_QC_PASS, STAGE_CLEAN, STAGE_DELETE)
    private val VALUES: Array<String?>? = arrayOf(
            STAGE_0, STAGE_1, STAGE_VERBATIM, STAGE_CLASSIFIED,
            STAGE_2, STAGE_QC_FAIL, STAGE_QC_PASS, STAGE_CLEAN,
            STAGE_DONE, STAGE_DELETE)
    private val VERBATIM_VALUES: Array<String?>? = arrayOf(STAGE_1, STAGE_VERBATIM)
    private val VERBATIM_CLASSIFIED_VALUES: Array<String?>? = arrayOf(STAGE_VERBATIM,
            STAGE_CLASSIFIED)

    /**
     * Obtain the list of all workflow status values that a user can put a record
     * into.
     *
     * @return array of string constants for all workflow status values that might
     * be
     * set by a user.
     */
    val workFlowStatusValues: Array<String?>?
        get() = CHANGABLE_VALUES

    /**
     * Obtain the list of all workflow status values that can be used when a
     * record may be put into a verbatim captured state.
     *
     * @return array of string constants
     */
    val verbatimWorkFlowStatusValues: Array<String?>?
        get() = VERBATIM_VALUES

    /**
     * Obtain the list of all workflow status values that can be used when a
     * record may be put into a verbatim classified state.
     *
     * @return array of string constants.
     */
    val verbatimClassifiedWorkFlowStatusValues: Array<String?>?
        get() = VERBATIM_CLASSIFIED_VALUES

    /**
     * Obtain the complete list of all possible workflow status values.
     *
     * @return arry of string constants for all workflow status states.
     */
    val allWorkFlowStatusValues: Array<String?>?
        get() = VALUES

    /**
     * Test to see whether or not a state can be changed to verbatim captured.
     *
     * @param workflowStatus a current workflow state to check
     * @return true if the record can be placed into state verbatim captured from
     * its current (other) state, false otherwise.  False if the current state
     * is already verbatim captured.
     */
    fun allowsVerbatimUpdate(workflowStatus: String): Boolean {
        var result = false
        if (workflowStatus == STAGE_0) {
            result = true
        }
        if (workflowStatus == STAGE_1) {
            result = true
        }
        return result
    }

    /**
     * Test to see whether or not a state can be changed to verbatim captured when
     * overwrites are allowed.
     *
     * @param workflowStatus a current workflow state to check
     * @return true if the record can be placed into state verbatim captured from
     * its current (other) state, false otherwise.  True if the current state
     * is already verbatim captured.
     */
    fun allowsVerbatimUpdateOverwrite(workflowStatus: String): Boolean {
        var result = false
        if (workflowStatus == STAGE_0) {
            result = true
        }
        if (workflowStatus == STAGE_1) {
            result = true
        }
        if (workflowStatus == STAGE_VERBATIM) {
            result = true
        }
        return result
    }

    /**
     * Test to see whether or not a state can be changed to verbatim classified.
     *
     * @param workflowStatus a current workflow state to check.
     * @return true if the record can be placed into state verbatim classified
     * from
     * its current (other) state, false otherwise.  True if the current state is
     * verbatim classified.
     */
    fun allowsClassifiedUpdate(workflowStatus: String): Boolean {
        var result = false
        if (workflowStatus == STAGE_0) {
            result = true
        }
        if (workflowStatus == STAGE_1) {
            result = true
        }
        if (workflowStatus == STAGE_VERBATIM) {
            result = true
        }
        if (workflowStatus == STAGE_CLASSIFIED) {
            result = true
        }
        return result
    }
}
