/**
 * UnitTrayLabel.java
 * edu.harvard.mcz.imagecapture.encoder
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
package edu.harvard.mcz.imagecapture.entity


import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import edu.harvard.mcz.imagecapture.interfaces.CollectionReturner
import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner
import org.apache.commons.logging.LogFactory
import java.util.*

/**
 * UnitTrayLabel
 *
 *
 * Includes factory method for decoding a set of key-value pairs in JSON, and a
 * method for constructing such a string with a JSON encoding.
 */
class UnitTrayLabel : TaxonNameReturner, DrawerNameReturner, CollectionReturner {
    /**
     * @return the id
     */
    /**
     * @param id the id to set
     */
    var id: Long? = null
    private var drawerNumber: String?
    private var family: String?
    private var subfamily: String?
    private var tribe: String?
    private var genus: String?
    private var specificEpithet: String?
    private var subspecificEpithet: String?
    private var infraspecificEpithet: String?
    private var infraspecificRank: String?
    private var authorship: String?
    private var unNamedForm: String? = null
    var printed = 0
    var numberToPrint = 0
    var dateCreated: Date?
    var dateLastUpdated: Date?
    /**
     * @param collection the collection to set
     */
    var collection // collection from which the material came
            : String?
    private var ordinal // order in which to print
            : Int? = null
    /**
     * @return the identifiedBy
     */
    /**
     * @param identifiedBy the identifiedBy to set
     */
    var identifiedBy: String? = null public get() {
        return field
    }

    constructor() {
        printed = 0
        numberToPrint = 1
        drawerNumber = ""
        family = ""
        subfamily = ""
        tribe = ""
        genus = ""
        specificEpithet = ""
        subspecificEpithet = ""
        infraspecificEpithet = ""
        infraspecificRank = ""
        authorship = ""
        unNamedForm = ""
        collection = ""
        ordinal = 1
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Constructor with all fields
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param unnamedForm
     * @param printed
     * @param collection
     */
    constructor(id: Long?, drawerNumber: String?, family: String?,
                subfamily: String?, tribe: String?, genus: String?,
                specificEpithet: String?, subspecificEpithet: String?,
                infraspecificEpithet: String?, infraspecificRank: String?,
                authorship: String?, unnamedForm: String?, printed: Int,
                dateCreated: Date?, dateLastUpdated: Date?,
                collection: String?, ordinal: Int?) : super() {
        this.id = id
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        this.subspecificEpithet = subspecificEpithet
        this.infraspecificEpithet = infraspecificEpithet
        this.infraspecificRank = infraspecificRank
        this.authorship = authorship
        unNamedForm = unnamedForm
        this.printed = printed
        this.dateCreated = dateCreated
        this.dateLastUpdated = dateLastUpdated
        this.collection = collection
        this.ordinal = ordinal
        this.dateCreated = Date()
        this.dateLastUpdated = Date()
    }

    /**
     * Constructor for infraspcific trinomial with explicit rank.
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param collection
     */
    constructor(drawerNumber: String?, family: String?, subfamily: String?,
                tribe: String?, genus: String?, specificEpithet: String?,
                infraspecificEpithet: String?, infraspecificRank: String?,
                authorship: String?, collection: String?) : super() {
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        subspecificEpithet = ""
        this.infraspecificEpithet = infraspecificEpithet
        this.infraspecificRank = infraspecificRank
        this.authorship = authorship
        this.collection = collection
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Constructor for species
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param authorship
     * @param collection
     */
    constructor(drawerNumber: String?, family: String?, subfamily: String?,
                tribe: String?, genus: String?, specificEpithet: String?,
                authorship: String?, collection: String?) : super() {
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        subspecificEpithet = ""
        infraspecificEpithet = ""
        infraspecificRank = ""
        this.authorship = authorship
        this.collection = collection
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Constructor for subspecies
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param authorship
     * @param collection
     */
    constructor(drawerNumber: String?, family: String?, subfamily: String?,
                tribe: String?, genus: String?, specificEpithet: String?,
                subspecificEpithet: String?, authorship: String?,
                collection: String?) : super() {
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        this.subspecificEpithet = subspecificEpithet
        infraspecificEpithet = ""
        infraspecificRank = ""
        this.authorship = authorship
        this.collection = collection
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Retuns a JSON encoding of the list of fields that can appear on a unit tray
     * label using key-value pairs where the keys are f,b,t,g,s,u,i,r,a,d, and
     * optionally c, and the values are respectively for the family,
     * subfamily,tribe, genus, specificepithet, subspecificepithet,
     * infraspecificepithet, infraspecificrank, authorship, drawernumber and
     * optionally collection.
     *
     * @return String containing JSON in the form { "f":"familyname", .... }
     * @see createFromJSONString
     */
    fun toJSONString(): String {
        val result = StringBuffer()
        result.append("{")
        result.append(" \"f\":\"").append(family).append("\"")
        result.append(", \"b\":\"").append(subfamily).append("\"")
        result.append(", \"t\":\"").append(tribe).append("\"")
        result.append(", \"g\":\"").append(genus).append("\"")
        result.append(", \"s\":\"").append(specificEpithet).append("\"")
        result.append(", \"u\":\"").append(subspecificEpithet).append("\"")
        result.append(", \"i\":\"").append(infraspecificEpithet).append("\"")
        result.append(", \"r\":\"").append(infraspecificRank).append("\"")
        result.append(", \"a\":\"").append(authorship).append("\"")
        result.append(", \"d\":\"").append(drawerNumber).append("\"")
        if (collection != null) {
            if (!collection!!.isEmpty()) {
                result.append(", \"c\":\"").append(collection).append("\"")
            }
        }
        if (identifiedBy != null && identifiedBy!!.trim { it <= ' ' }.length > 0) {
            result.append(", \"id\":\"").append(identifiedBy).append("\"")
        }
        result.append(" }")
        return result.toString()
    }

    /**
     * @return the drawerNumber
     */
    override fun getDrawerNumber(): String? {
        return drawerNumber
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    fun setDrawerNumber(drawerNumber: String?) {
        this.drawerNumber = drawerNumber
        if (this.drawerNumber != null) {
            this.drawerNumber = this.drawerNumber!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the family
     */
    override fun getFamily(): String? {
        return family
    }

    /**
     * @param family the family to set
     */
    fun setFamily(family: String?) {
        this.family = family
        if (this.family != null) {
            this.family = this.family!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the subfamily
     */
    override fun getSubfamily(): String? {
        return subfamily
    }

    /**
     * @param subfamily the subfamily to set
     */
    fun setSubfamily(subfamily: String?) {
        this.subfamily = subfamily
        if (this.subfamily != null) {
            this.subfamily = this.subfamily!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the tribe
     */
    override fun getTribe(): String? {
        if (tribe == null) {
            tribe = ""
        }
        return tribe
    }

    /**
     * @param tribe the tribe to set
     */
    fun setTribe(tribe: String?) {
        this.tribe = tribe
        if (this.tribe != null) {
            this.tribe = this.tribe!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the genus
     */
    override fun getGenus(): String? {
        if (genus == null) {
            genus = ""
        }
        return genus
    }

    /**
     * @param genus the genus to set
     */
    fun setGenus(genus: String?) {
        this.genus = genus
        if (this.genus != null) {
            this.genus = this.genus!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the specificEpithet
     */
    override fun getSpecificEpithet(): String? {
        if (specificEpithet == null) {
            specificEpithet = ""
        }
        return specificEpithet
    }

    /**
     * @param specificEpithet the specificEpithet to set
     */
    fun setSpecificEpithet(specificEpithet: String?) {
        this.specificEpithet = specificEpithet
        if (this.specificEpithet != null) {
            this.specificEpithet = this.specificEpithet!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the subspecificEpithet
     */
    override fun getSubspecificEpithet(): String? {
        if (subspecificEpithet == null) {
            subspecificEpithet = ""
        }
        return subspecificEpithet
    }

    /**
     * @param subspecificEpithet the subspecificEpithet to set
     */
    fun setSubspecificEpithet(subspecificEpithet: String?) {
        this.subspecificEpithet = subspecificEpithet
        if (this.subspecificEpithet != null) {
            this.subspecificEpithet = this.subspecificEpithet!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the infraspecificEpithet
     */
    override fun getInfraspecificEpithet(): String? {
        if (infraspecificEpithet == null) {
            infraspecificEpithet = ""
        }
        return infraspecificEpithet
    }

    /**
     * @param infraspecificEpithet the infraspecificEpithet to set
     */
    fun setInfraspecificEpithet(infraspecificEpithet: String?) {
        this.infraspecificEpithet = infraspecificEpithet
        if (this.infraspecificEpithet != null) {
            this.infraspecificEpithet = this.infraspecificEpithet!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the infraspecifcRank
     */
    override fun getInfraspecificRank(): String? {
        if (infraspecificRank == null) {
            infraspecificRank = ""
        }
        return infraspecificRank
    }

    /**
     * @param infraspecifcRank the infraspecifcRank to set
     */
    fun setInfraspecificRank(infraspecifcRank: String?) {
        infraspecificRank = infraspecifcRank
        if (infraspecificRank != null) {
            infraspecificRank = infraspecificRank!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the authorship
     */
    override fun getAuthorship(): String? {
        if (authorship == null) {
            authorship = ""
        }
        return authorship
    }

    /**
     * @param authorship the authorship to set
     */
    fun setAuthorship(authorship: String?) {
        this.authorship = authorship
        if (this.authorship != null) {
            this.authorship = this.authorship!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the unnamedForm
     */
    fun getUnNamedForm(): String? {
        if (unNamedForm == null) {
            unNamedForm = ""
        }
        return unNamedForm
    }

    /**
     * @param unnamedForm the unnamedForm to set
     */
    fun setUnNamedForm(unNamedForm: String?) {
        this.unNamedForm = unNamedForm
    }

    /**
     * @return the ordinal
     */
    fun getOrdinal(): Int? {
        if (ordinal == null) {
            ordinal = 0
        }
        return ordinal
    }

    /**
     * @param ordinal the ordinal to set
     */
    fun setOrdinal(ordinal: Int?) {
        if (ordinal == null) {
            this.ordinal = 0
        } else {
            this.ordinal = ordinal
        }
    }

    companion object {
        private val log = LogFactory.getLog(UnitTrayLabel::class.java)
        /**
         * Factory method, given a JSON encoded string, as encoded with
         * toJSONString(), extract the values from that string into a new instance of
         * UnitTrayLabel so that they can be obtained by the appropriate returner
         * interface (taxonnameReturner, drawernumberReturner, collectionReturner).
         *
         * @param jsonEncodedLabel the JSON to decode.
         * @return a new UnitTrayLabel populated with the values found in the supplied
         * jsonEncodedLabel text or null on a failure.
         * @see toJSONString
         */
        fun createFromJSONString(jsonEncodedLabel: String): UnitTrayLabel? {
            var jsonEncodedLabel = jsonEncodedLabel
            var result: UnitTrayLabel? = null
            if (jsonEncodedLabel.matches("\\{.*\\}")) {
                val originalJsonEncodedLabel = jsonEncodedLabel
                jsonEncodedLabel = jsonEncodedLabel.replaceFirst("^\\{".toRegex(), "") // Strip off leading  {
                jsonEncodedLabel = jsonEncodedLabel.replaceFirst("\\}$".toRegex(), "") // Strip off trailing }
                if (jsonEncodedLabel.contains("}")) { // nested json, not expected.
                    log!!.error(
                            "JSON for UnitTrayLabel contains unexpected nesting { { } }.  JSON is: " +
                                    originalJsonEncodedLabel)
                } else {
                    log!!.debug(jsonEncodedLabel)
                    result = UnitTrayLabel()
                    // Beginning and end are special case for split on '", "'
// remove leading quotes and spaces (e.g. either trailing '" ' or
// trailing '"').
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst("^ ".toRegex(), "") // Strip off leading space
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst(" $".toRegex(), "") // Strip off trailing space
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst("^\"".toRegex(), "") // Strip off leading quote
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst(
                            "\"$".toRegex(), "") // Strip off trailing quote
                    // Convert any ", " into "," then split on ","
                    jsonEncodedLabel = jsonEncodedLabel.replace("\",\\s\"".toRegex(), "\",\"")
                    log.debug(jsonEncodedLabel)
                    // split into key value parts by '","'
                    val pairs: Array<String?> = jsonEncodedLabel.split("\",\"".toRegex()).toTypedArray()
                    for (x in pairs.indices) { // split each key value pair
                        val keyValuePair: Array<String?> = pairs[x]!!.split("\":\"".toRegex()).toTypedArray()
                        if (keyValuePair.size == 2) {
                            val key = keyValuePair[0]
                            val value = keyValuePair[1]
                            log.debug("key=[$key], value=[$value]")
                            if (key == "m1p") {
                                log.debug("Configured for Project: $value")
                                if (value != "MCZ Lepidoptera" &&
                                        value != "ETHZ Entomology" &&
                                        value != "[ETHZ Entomology]") {
                                    log.error("Project specified in JSON is not recognized: " +
                                            value)
                                    log.error(
                                            "Warning: Keys in JSON may not be correctly interpreted.")
                                }
                            }
                            // Note: Adding values here isn't sufficient to populate specimen
// records, you still need to invoke the relevant returner interface
// on the parser.
                            if (key == "f") {
                                result.setFamily(value)
                            }
                            if (key == "b") {
                                result.setSubfamily(value)
                            }
                            if (key == "t") {
                                result.setTribe(value)
                            }
                            if (key == "g") {
                                result.setGenus(value)
                            }
                            if (key == "s") {
                                result.setSpecificEpithet(value)
                            }
                            if (key == "u") {
                                result.setSubspecificEpithet(value)
                            }
                            if (key == "i") {
                                result.setInfraspecificEpithet(value)
                            }
                            if (key == "r") {
                                result.setInfraspecificRank(value)
                            }
                            if (key == "a") {
                                result.setAuthorship(value)
                            }
                            if (key == "d") {
                                result.setDrawerNumber(value)
                            }
                            if (key == "c") {
                                result.collection = value
                                log.debug(result.collection)
                            }
                            if (key == "id") {
                                result.identifiedBy = value
                            }
                        }
                    }
                    log.debug(result.toJSONString())
                }
            } else {
                log!!.debug("JSON not matched to { }")
            }
            return result
        }
    }
}
