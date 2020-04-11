/**
 * MetadataRetriever.java
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
package edu.harvard.mcz.imagecapture.data


import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.entity.*
import edu.harvard.mcz.imagecapture.entity.Number
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import java.awt.Color
import java.text.ParseException
import javax.swing.InputVerifier
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.text.MaskFormatter

/**
 * MetadataRetriever produces metadata (field lengths, tooltip texts, input masks, input verifiers)
 * for fields in tables in database.
 */
object MetadataRetriever {
    private fun repeat(s: String?, count: Int): String {
        val result = StringBuffer()
        for (i in 0 until count) {
            result.append(s)
        }
        return result.toString()
    }

    /**
     * Generates a MaskFormatter for a JFormattedTextField based upon the length (and potentially the type) of the
     * underlying text field.  Doesn't work well for normal varchar() fields, as the JTextField appears to be full
     * of spaces.
     *
     *
     * Usage:
     * <pre>
     * JFormattedTextField jtext_for_fieldname = new JFormattedTextField(MetadataRetriever(tablename.class,"fieldname"));
    </pre> *
     *
     * @param aTableClass
     * @param fieldname
     * @return a MaskFormatter for a jFormattedTextField
     *
     *
     * TODO: add field type recognition, currently returns only "****" masks.
     */
    fun getMask(aTableClass: Class<*>?, fieldname: String): MaskFormatter? {
        var formatter: MaskFormatter? = null
        try {
            formatter = MaskFormatter(repeat("*", getFieldLength(aTableClass, fieldname)))
        } catch (e: ParseException) { // Shouldn't end up here unless tables have been redesigned and
// MetadataRetriever.getFieldLength isn't returning a value.
            println("Bad Mask format string")
            e.printStackTrace()
        }
        return formatter
    }

    /**
     * Generates an InputVerifier for a JTextField
     *
     *
     * Usage:
     * <pre>
     * JTextField jText_for_fieldname = new JTextField();
     * jText_for_fieldname.addInputVerifier(MetadataRetriever.getInputVerifier(tablename.class,"fieldname",jText_for_fieldname));
    </pre> *
     *
     * @param aTableClass table proxy object for fieldname
     * @param fieldname   field for which to check the fieldlength
     * @param field       JTextField to which the InputVerifier is being added.
     * @return an InputVerifier for the JTextField
     * TODO: implement tests for more than just length.
     */
    fun getInputVerifier(aTableClass: Class<*>?, fieldname: String, field: JTextField): InputVerifier? {
        var result: InputVerifier? = null
        if (aTableClass == Specimen::class.java && (fieldname.equals("ISODate", ignoreCase = true) || fieldname.equals("DateIdentified", ignoreCase = true))
                || aTableClass == Determination::class.java && fieldname.equals("DateIdentified", ignoreCase = true)) {
            result = object : InputVerifier() {
                override fun verify(comp: JComponent?): Boolean {
                    var returnValue = true
                    val textField: JTextField = comp as JTextField?
                    val content: String = textField.Text
                    if (content.length > getFieldLength(aTableClass, fieldname)) {
                        returnValue = false
                    } else {
                        if (!content.matches(ImageCaptureApp.REGEX_DATE)) {
                            returnValue = false
                        }
                    }
                    return returnValue
                }

                override fun shouldYieldFocus(input: JComponent?): Boolean {
                    val valid: Boolean = super.shouldYieldFocus(input)
                    if (valid) {
                        field.setBackground(Color.WHITE)
                    } else {
                        field.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                    }
                    field.revalidate()
                    return valid
                }
            }
        } else {
            result = object : InputVerifier() {
                override fun verify(comp: JComponent?): Boolean {
                    var returnValue = true
                    val textField: JTextField = comp as JTextField?
                    val content: String = textField.Text
                    if (content.length > getFieldLength(aTableClass, fieldname)) {
                        returnValue = false
                    }
                    return returnValue
                }

                override fun shouldYieldFocus(input: JComponent?): Boolean {
                    val valid: Boolean = super.shouldYieldFocus(input)
                    if (valid) {
                        if (fieldname.equals("Inferences", ignoreCase = true)) {
                            field.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
                        } else {
                            field.setBackground(Color.WHITE)
                        }
                    } else {
                        field.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                    }
                    field.revalidate()
                    return valid
                }
            }
        }
        return result
    }

    /**
     * Determine the length of a field from the class of the proxy object
     * for the table and the name of the field.
     *
     *
     * Usage:
     * <pre>
     * int genusSize = MetadataRetriever.getFieldLength(Specimen.class, "genus");
    </pre> *
     *
     * @param aTableClass the class of the proxy object over the table.
     * @param fieldname   the name of the field in that table (case insensitive).
     * @return the number of characters that can be put into the field.
     */
    fun getFieldLength(aTableClass: Class<*>?, fieldname: String): Int {
        var length = 0
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("Barcode", ignoreCase = true)) {
                length = 20
            }
            if (fieldname.equals("DrawerNumber", ignoreCase = true)) {
                length = 10
            }
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("TypeNumber", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("CitedInPublication", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("Features", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Family", ignoreCase = true)) {
                length = 40
            }
            if (fieldname.equals("Subfamily", ignoreCase = true)) {
                length = 40
            }
            if (fieldname.equals("Tribe", ignoreCase = true)) {
                length = 40
            }
            length = getDetFieldLength(fieldname, length)
            if (fieldname.equals("NatureOfID", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("Country", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("PrimaryDivison", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("SpecificLocality", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("VerbatimElevation", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("CollectingMethod", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("ISODate", ignoreCase = true)) {
                length = 21
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                length = 32
            }
            if (fieldname.equals("DateEmerged", ignoreCase = true)) {
                length = 32
            }
            if (fieldname.equals("DateEmergedIndicator", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("DateCollected", ignoreCase = true)) {
                length = 32
            }
            if (fieldname.equals("DateCollectedIndicator", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Collection", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("SpecimenNotes", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("LifeStage", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Sex", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("PreparationType", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Habitat", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("AssociatedTaxon", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("Questions", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("Inferences", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("LocationInCollection", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("WorkFlowStatus", ignoreCase = true)) {
                length = 30
            }
            if (fieldname.equals("CreatedBy", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("DateLastUpdated", ignoreCase = true)) {
                length = 0
            }
            if (fieldname.equals("LastUpdatedBy", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("ValidDistributionFlag", ignoreCase = true)) {
                length = 1
            }
        }
        if (aTableClass == Number::class.java) {
            if (fieldname.equals("Number", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("NumberType", ignoreCase = true)) {
                length = 50
            }
        }
        if (aTableClass == Collector::class.java) {
            if (fieldname.equals("CollectorName", ignoreCase = true)) {
                length = 255
            }
        }
        if (aTableClass == Determination::class.java) {
            length = getDetFieldLength(fieldname, length)
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("NatureOfID", ignoreCase = true)) {
                length = 255
            }
        }
        if (aTableClass == Users::class.java) {
            if (fieldname.equals("username", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("fullname", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("description", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("role", ignoreCase = true)) {
                length = 20
            }
        }
        if (aTableClass == ICImage::class.java) {
            if (fieldname.equals("rawBarcode", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("rawExifBarcode", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("filename", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("rawOcr", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("path", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("uri", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("templateId", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("drawerNumber", ignoreCase = true)) {
                length = 10
            }
        }
        return length
    }

    /**
     * Extracted from above: field lengths for both Specimen & Determination
     *
     * @param fieldname
     * @param length
     * @return
     */
    private fun getDetFieldLength(fieldname: String, length: Int): Int {
        var length = length
        if (fieldname.equals("Genus", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("SpecificEpithet", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("SubspecificEpithet", ignoreCase = true)) {
            length = 255
        }
        if (fieldname.equals("InfraspecificEpithet", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("InfraspecificRank", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("Authorship", ignoreCase = true)) {
            length = 255
        }
        if (fieldname.equals("UnNamedForm", ignoreCase = true)) {
            length = 50
        }
        if (fieldname.equals("IdentificationQualifier", ignoreCase = true)) {
            length = 50
        }
        if (fieldname.equals("IdentifiedBy", ignoreCase = true)) {
            length = 255
        }
        if (fieldname.equals("DateIdentified", ignoreCase = true)) {
            length = 21
        }
        return length
    }

    /**
     * Given a proxy class for a table and the name of a field return a help text for that field.
     *
     * @param aTableClass
     * @param fieldname
     * @return a String containing a help text suitable for use as a tooltip or other context help display.
     */
    fun getFieldHelp(aTableClass: Class<*>?, fieldname: String): String {
        var help = "No help available"
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("Barcode", ignoreCase = true)) {
                help = "The barcode of the specimen, in the form 'MCZ-ENT01234567'"
            }
            if (fieldname.equals("DrawerNumber", ignoreCase = true)) {
                help = "The drawer number from the specimen unit tray label."
            }
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                help = "Not a type, or the kind of type (e.g. Holotype) that this specimen is.  Normal workflow uses value 'not a type'.  This type status refers to the type status of this specimen for the name in the specimen record (secondary types may have type status set in other determinations)."
            }
            if (fieldname.equals("TypeNumber", ignoreCase = true)) {
                help = "The number from the type catalog number series that applies to this specimen"
            }
            if (fieldname.equals("CitedInPublication", ignoreCase = true)) {
                help = "Publications this specimen is cited in, as found on labels.  Separate publications with a semicolon ' ; '"
            }
            if (fieldname.equals("Features", ignoreCase = true)) {
                help = "Description of features of this specimen that aren't named forms, un-named forms, sex, or life stage.  Examples: features could include eclosion defect, runt (unusually small), deformed, faded colours, scales stripped for venation, greasy, stained, psocid damaged."
            }
            if (fieldname.equals("Family", ignoreCase = true)) {
                help = "The family into which this specimen is placed, from the unit tray label"
            }
            if (fieldname.equals("Subfamily", ignoreCase = true)) {
                help = "The subfamily into which this specimen is placed, from the unit tray label"
            }
            if (fieldname.equals("Tribe", ignoreCase = true)) {
                help = "The tribe into which this specimen is placed, if any, from the unit tray label."
            }
            if (fieldname.equals("Genus", ignoreCase = true)) {
                help = "The generic name from the unit tray label.  The current identification for non-primary types, the type name for primary types.  Example: 'Delias' from 'Delias argenthona Fabricius, 1793'"
            }
            if (fieldname.equals("SpecificEpithet", ignoreCase = true)) {
                help = "The specific part of the species group name from the unit tray label. The current identification for non-primary types, the type name for primary types.  Include indicators of uncertanty associated with this part of the name such as nr, cf, ?.  May be 'sp.'  Example: 'argenthoma' from 'Delias argenthona Fabricius, 1793'"
            }
            if (fieldname.equals("SubspecificEpithet", ignoreCase = true)) {
                help = "The subspecific part (if present) of the species group name from the unit tray label.  Include indicators of uncertanty associated with this part of the name such as nr, cf, ?.  May be 'ssp.'"
            }
            if (fieldname.equals("InfraspecificEpithet", ignoreCase = true)) {
                help = "The form, varietal, or other part of a name with a rank below subspecies from the unit tray label"
            }
            if (fieldname.equals("InfraspecificRank", ignoreCase = true)) {
                help = "The rank (e.g. form, variety) of the infraspecific name from the unit tray label."
            }
            if (fieldname.equals("Authorship", ignoreCase = true)) {
                help = "The author of the species group name from the unit tray label.  Include year and parenthesies if present.  Example: 'Fabricius, 1793' from 'Delias argenthona Fabricius, 1793'"
            }
            if (fieldname.equals("UnNamedForm", ignoreCase = true)) {
                help = "e.g. 'Winter form', informal descriptive name of the form of this specimen (for informal form names not regulated by the ICZN)."
            }
            if (fieldname.equals("IdentificationQualifier", ignoreCase = true)) {
                help = "Don't use this field."
            }
            if (fieldname.equals("IdentifiedBy", ignoreCase = true)) {
                help = "Name of the person, if known, who made this identification."
            }
            if (fieldname.equals("DateIdentified", ignoreCase = true)) {
                help = "Date at which this identification was made, if known.  Use ISO format yyyy/mm/dd-yyyy/mm/dd."
            }
            if (fieldname.equals("IdentificationRemarks", ignoreCase = true)) {
                help = "Remarks about this identification."
            }
            if (fieldname.equals("NatureOfId", ignoreCase = true)) {
                help = "Nature of the Identification: expert ID=made by known expert; legacy=on label with no data on identifier. "
            }
            if (fieldname.equals("Country", ignoreCase = true)) {
                help = "The country from which this specimen was collected.  Infer if you have specialist knowledge and annotate in Inferences"
            }
            if (fieldname.equals("PrimaryDivison", ignoreCase = true)) {
                help = "The state, province, or other primary geopolitical division of the country from which this specimen was collected.  Infer if you have specialist knowlege and annotate in Inferences"
            }
            if (fieldname.equals("SpecificLocality", ignoreCase = true)) {
                help = "Placenames, offsets, and other text describing where this specimen was collected.  Press button to use '[no specific locality data]' when there are no specific locality data."
            }
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                help = "Verbatim transcription of locality information found on this specimen's labels."
            }
            if (fieldname.equals("VerbatimElevation", ignoreCase = true)) {
                help = "Verbatim transcription of elevation information, including units, found on this specimen's labels"
            }
            if (fieldname.equals("CollectingMethod", ignoreCase = true)) {
                help = "If specified on a label, the method by which this specimen was collected."
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                help = "The default date field, a verbatim date associated with this specimen that isn't marked as either a date collected or date emerged, and might be either of these or some other date."
            }
            if (fieldname.equals("ISODate", ignoreCase = true)) {
                help = "The date collected or the default date in ISO date format yyyy/mm/dd. Optionally, a range yyyy/mm/dd-yyyy/mm/dd"
            }
            if (fieldname.equals("DateEmerged", ignoreCase = true)) {
                help = "The date at which this butterfly emerged."
            }
            if (fieldname.equals("DateEmergedIndicator", ignoreCase = true)) {
                help = "The verbatim text from the label that indicates that this is a date emerged."
            }
            if (fieldname.equals("DateCollected", ignoreCase = true)) {
                help = "The date at which this butterfly was collected from the wild."
            }
            if (fieldname.equals("DateCollectedIndicator", ignoreCase = true)) {
                help = "The verbatim text from the label that indicates that this is a date collected."
            }
            if (fieldname.equals("Collection", ignoreCase = true)) {
                help = "The name of a collection of which this specimen was a part.  Use a standard format rather than verbatim text."
            }
            if (fieldname.equals("SpecimenNotes", ignoreCase = true)) {
                help = "Notes from the labels about this specimen.  Use a semicolon ; to separate multiple notes."
            }
            if (fieldname.equals("LifeStage", ignoreCase = true)) {
                help = "The life stage of this specimen."
            }
            if (fieldname.equals("Sex", ignoreCase = true)) {
                help = "The sex of this specimen."
            }
            if (fieldname.equals("PreparationType", ignoreCase = true)) {
                help = "The preparation type of this specimen."
            }
            if (fieldname.equals("Habitat", ignoreCase = true)) {
                help = "Text from the labels descrbing the habitat from which this specimen was collected."
            }
            if (fieldname.equals("AssociatedTaxon", ignoreCase = true)) {
                help = "If this specimen represents an associated taxon such as a host ant, put the actual identification of this specimen, to whatever level it is available here, and put the name of the butterfly from the unit tray label in the identification (i.e. genus, species, etc. fields)."
            }
            if (fieldname.equals("Questions", ignoreCase = true)) {
                help = "Describe any questions or problems you have with the transcription of the data from this specimen."
            }
            if (fieldname.equals("Inferences", ignoreCase = true)) {
                help = "If you have specialist knowledge about this specimen, briefly describe the basis for any inferences you are making in adding data to this record that isn't present on the specimen labels.  Example: 'Locality for this species known to be in Australia, pers obs.'"
            }
            if (fieldname.equals("LocationInCollection", ignoreCase = true)) {
                help = "General Collection, Type Collection, or other major storage division of the Lepidoptera collection."
            }
            if (fieldname.equals("WorkFlowStatus", ignoreCase = true)) {
                help = "The current state of this database record in the workflow."
            }
            if (fieldname.equals("CreatedBy", ignoreCase = true)) {
                help = "The name of the person or automated process that created this record."
            }
            if (fieldname.equals("DateLastUpdated", ignoreCase = true)) {
                help = "The date and time at which this record was most recently updated."
            }
            if (fieldname.equals("LastUpdatedBy", ignoreCase = true)) {
                help = "The name of the person who most recenly updated this record."
            }
            if (fieldname.equals("ValidDistributionFlag", ignoreCase = true)) {
                help = "Uncheck if the locality does not reflect the collection of this specimen from nature (e.g. uncheck for specimens that came from a captive breeding program).  Leave checked if locality represents natural biological range. "
            }
        }
        if (aTableClass == Number::class.java) {
            if (fieldname.equals("Number", ignoreCase = true)) {
                help = "A number (including alphanumeric identifiers) found on a label of this specimen."
            }
            if (fieldname.equals("NumberType", ignoreCase = true)) {
                help = "If known, what sort of number this is and where it came from."
            }
        }
        if (aTableClass == Collector::class.java) {
            if (fieldname.equals("CollectorName", ignoreCase = true)) {
                help = "The name of a collector."
            }
        }
        if (aTableClass == Determination::class.java) {
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                help = "Not a type, or the kind of type (e.g. Topotype) that this specimen is for this particular name."
            }
            if (fieldname.equals("Genus", ignoreCase = true)) {
                help = "The generic name used in the identification."
            }
            if (fieldname.equals("SpecificEpithet", ignoreCase = true)) {
                help = "The specific part of the species group name used in the identification."
            }
            if (fieldname.equals("SubspecificEpithet", ignoreCase = true)) {
                help = "The subspecific part (if present) of the species group name used in the identification."
            }
            if (fieldname.equals("InfraspecificEpithet", ignoreCase = true)) {
                help = "The form, varietal, or other part of a name with a rank below subspecies used in the identification."
            }
            if (fieldname.equals("InfraspecificRank", ignoreCase = true)) {
                help = "The rank (e.g. form, variety, abberation, morph, lusus, natio) of the infrasubspecific name."
            }
            if (fieldname.equals("Authorship", ignoreCase = true)) {
                help = "The author of the species group name used in the determination."
            }
            if (fieldname.equals("UnNamedForm", ignoreCase = true)) {
                help = "e.g. 'Winter form', informal descriptive name of the form of this specimen (not regulated by the ICZN)."
            }
            if (fieldname.equals("IdentificationQualifier", ignoreCase = true)) {
                help = "A question mark or other qualifier that indicates the identification of this specimen is uncertain."
            }
            if (fieldname.equals("IdentifiedBy", ignoreCase = true)) {
                help = "Name of the person, if known, who made this identification."
            }
            if (fieldname.equals("DateIdentified", ignoreCase = true)) {
                help = "Date at which this identification was made, if known.  Use ISO Format yyyy/mm/dd-yyyy/mm/dd."
            }
            if (fieldname.equals("IdentificationRemarks", ignoreCase = true)) {
                help = "Remarks about this identification."
            }
            if (fieldname.equals("NatureOfId", ignoreCase = true)) {
                help = "Nature of the Identification: expert ID=made by known expert; legacy=on label with no data on identifier. "
            }
        }
        if (aTableClass == Users::class.java) {
            if (fieldname.equals("username", ignoreCase = true)) {
                help = "Database username of this person."
            }
            if (fieldname.equals("fullname", ignoreCase = true)) {
                help = "The full name of this person."
            }
            if (fieldname.equals("description", ignoreCase = true)) {
                help = "What this person's role in the project and specialties are."
            }
            if (fieldname.equals("role", ignoreCase = true)) {
                help = "Unused"
            }
        }
        return help
    }

    /**
     * Test to see whether a field allowed to be updated by an external process.
     *
     * @param aTableClass the class for the table in which the field occurs.
     * @param fieldname   the name of the field (case insensitive).
     * @return true if the field is allowed to be updated by an external process, false otherwise.
     */
    fun isFieldExternallyUpdatable(aTableClass: Class<*>?, fieldname: String): Boolean {
        var result = false
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("TypeNumber", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("CitedInPublication", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Features", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("HigherGeography", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("SpecificLocality", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollector", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimNumbers", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimUnclassifiedText", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimClusterIdentifier", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowProcess", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowDate", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Minimum_Elevation", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Maximum_Elevation", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("MinimumElevationSt", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("MaximumElevationSt", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Elev_Units", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("CollectingMethod", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ISODate", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateEmerged", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateEmergedIndicator", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateCollected", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateCollectedIndicator", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Collection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("SpecimenNotes", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("LifeStage", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Sex", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("PreparationType", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Habitat", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Microhabitat", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("AssociatedTaxon", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Questions", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Inferences", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("LocationInCollection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ValidDistributionFlag", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Collector::class.java) {
            if (fieldname.equals("CollectorName", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Determination::class.java) {
            if (fieldname.equals("VerbatimText", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Number::class.java) {
            if (fieldname.equals("Number", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("NumberType", ignoreCase = true)) {
                result = true
            }
        }
        return result
    }

    /**
     * Test to see whether a field in a table is intended to hold verbatim values.
     *
     * @param aTableClass the class for the table.
     * @param fieldname   the field to check (not case sensitive)
     * @return true if the field is intended to hold verbatim data, false otherwise.
     */
    fun isFieldVerbatim(aTableClass: Class<*>?, fieldname: String): Boolean {
        var result = false
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollector", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimNumbers", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimUnclassifiedText", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Collector::class.java) {
        }
        if (aTableClass == Determination::class.java) {
            if (fieldname.equals("VerbatimText", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Number::class.java) {
        }
        return result
    }

    /**
     * Identify whether a field in a table contains values that are process metadata.
     *
     * @param aTableClass table to check
     * @param fieldname   field in aTableClass
     * @return true if process metadata field, false otherwise.
     */
    fun isFieldProcessMetadata(aTableClass: Class<*>?, fieldname: String): Boolean {
        var result = false
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("VerbatimClusterIdentifier", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowProcess", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowDate", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("WorkflowStatus", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Collector::class.java) {
        }
        if (aTableClass == Determination::class.java) {
        }
        if (aTableClass == Number::class.java) {
        }
        return result
    }
}
