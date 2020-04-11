/**
 *
 */
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.UnitTrayLabelParser
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import edu.harvard.mcz.imagecapture.interfaces.CollectionReturner
import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner
import org.apache.commons.logging.LogFactory

/**Parses the text on an MCZ Lepidoptera unit tray label into atomic higher taxon and species group name
 * elements.
 *
 *
 *
 */
class UnitTrayLabelParser(private var text: String?) : TaxonNameReturner, DrawerNameReturner, CollectionReturner {
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getFamily()
     */ var family: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getSubfamily()
     */ var subfamily: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getTribe()
     */ var tribe: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getGenus()
     */ var genus: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getSpecificEpithet()
     */ var specificEpithet: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getSubspecificEpithet()
     */ var subspecificEpithet: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getAuthorship()
     */ var authorship: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getInfraspecificEpithet()
     */ var infraspecificEpithet: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getInfraspecificRank()
     */ var infraspecificRank: String? = null public get() {
        return field
    }
        private set
    private var drawerNumber: String? = null
    /**
     * @param collection the collection to set
     */
    var collection // collection from which the material came
            : String? = null public get() {
        return field
    }
    /**
     * @return the identifiedBy
     */
    /**
     * @param identifiedBy the identifiedBy to set
     */
    var identifiedBy: String? = null public get() {
        return field
    }
    /**
     * Was this Parse done from JSON.
     *
     * @return true if parsed from json, otherwise false.
     */
    var isParsedFromJSON = false
        private set

    protected fun parseFromJSON(json: String): Boolean {
        var result = false
        val label: UnitTrayLabel = UnitTrayLabel.Companion.createFromJSONString(json)
        if (label != null) {
            result = true
            family = label.Family
            subfamily = label.Subfamily
            tribe = label.Tribe
            genus = label.Genus
            specificEpithet = label.SpecificEpithet
            subspecificEpithet = label.SubspecificEpithet
            authorship = label.Authorship
            infraspecificEpithet = label.InfraspecificEpithet
            infraspecificRank = label.InfraspecificRank
            drawerNumber = label.DrawerNumber
            isParsedFromJSON = true
        }
        return result
    }

    /**
     * Identify atomic database field elements from their position in aStringToParse as
     * given to the constructor (called by the constructor).  Invokes the protected set_ methods.
     */
    protected fun parse() {
        family = ""
        subfamily = ""
        tribe = ""
        genus = ""
        specificEpithet = ""
        subspecificEpithet = ""
        authorship = ""
        infraspecificEpithet = ""
        infraspecificRank = ""
        drawerNumber = ""
        // Can't parse text if it is null.
        if (text != null) { // trim out some likely OCR errors
            text = text!!.replace("|", "")
            text = text!!.replace("í", "i") // ocr on utf8 filesystem
            //text = text.replace("Ã-", "i");   // "tilda A -" from ocr on windows filesystem
            val higherbits: Array<String?> = text!!.split(":".toRegex()).toTypedArray()
            var nameStartsAt = 0
            // look for the higher taxon bits
            if (higherbits.size > 0) { // get text before first colon
// Check for extraneous leading line and remove if present.
                val possFamily: Array<String?> = higherbits[0]!!.trim { it <= ' ' }.split("\n".toRegex()).toTypedArray()
                if (possFamily.size == 2) {
                    setFamily(possFamily[1]!!.trim { it <= ' ' })
                } else {
                    if (possFamily[0]!!.trim { it <= ' ' }.contains(" ")) { // Check for the case of a failed OCR of the separator colon.
// Start by setting the family.
                        setFamily(higherbits[0]!!.trim { it <= ' ' })
                        // now check.
                        val bits: Array<String?> = possFamily[0]!!.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
                        if (bits.size == 2 && bits[0]!!.length > 4 && bits[1]!!.length > 4) {
                            setFamily(bits[0]!!.trim { it <= ' ' })
                            setSubfamily(bits[1]!!.trim { it <= ' ' })
                        }
                    } else {
                        setFamily(higherbits[0]!!.trim { it <= ' ' })
                    }
                }
                nameStartsAt = 1
            }
            if (higherbits.size > 1) {
                if (higherbits.size > 2) { // get first word after colon
                    setSubfamily(higherbits[1]!!.trim { it <= ' ' }.split(" +".toRegex()).toTypedArray()[0]) // split on one or more spaces
                } else { // get everything else on the first line
// handles the pathological case of a space within the subfamily name.
                    setSubfamily(higherbits[1]!!.trim { it <= ' ' }.split("\n".toRegex()).toTypedArray()[0])
                }
                nameStartsAt = 1
            }
            if (higherbits.size > 2) { // Two colons, should be a tribe.
                val temp: Array<String?> = higherbits[2]!!.split("\n".toRegex()).toTypedArray()
                if (temp.size > 0 && temp[0]!!.trim { it <= ' ' } == "") { // second colon was followed by a newline character with no preceding text.
                    if (temp.size > 1 && temp[1]!!.trim { it <= ' ' }.contains(" ")) { // likely pathological case of two colons and no tribe 'family:subfamily:\n genus'
// second line contains a genus/species
// 'family:subfamily:' ends with colon but has no tribe.
                        nameStartsAt = 2 // split of higher bits still puts genus in next element.
                        // but there is no tribe to set.
                    } else { // Likely Tribe on second line, thus an extra leading newline.
// family: subfamily: \n tribe \n genus species
                        setTribe(higherbits[2]!!.trim { it <= ' ' }.split("[ \n]".toRegex()).toTypedArray()[0]) // split on space or new line
                        nameStartsAt = 2
                        // trim the leading newline off the beginning of the stuff that follows the second colon.
                        higherbits[2] = higherbits[2]!!.trim { it <= ' ' }
                    }
                } else { // Should be 'tribe \n genus'
// get first word after second colon, if any
                    setTribe(higherbits[2]!!.trim { it <= ' ' }.split("[ \n]".toRegex()).toTypedArray()[0]) // split on space or new line
                    nameStartsAt = 2
                }
            }
            var lines: Array<String?>? = null
            // Test for species group name spread on two lines.
            try {
                lines = higherbits[nameStartsAt]!!.split("\n".toRegex()).toTypedArray()
                if (lines.size > 4) {
                    val st = 1
                    if (lines[st]!!.trim { it <= ' ' }.contains(" ") &&
                            lines[st + 1]!!.trim { it <= ' ' }.matches(".*[A-Za-z]+.*") &&
                            lines[st + 2]!!.trim { it <= ' ' }.matches(".*[A-Za-z]+.*[0-9]+.*") &&
                            lines[st + 3]!!.trim { it <= ' ' }.matches(".*[0-9]+.*")) {
                        higherbits[nameStartsAt] = lines[0].toString() + "\n" + lines[st] + " " + lines[st + 1] + "\n" + lines[st + 2] + "\n" + lines[st + 3]
                    }
                }
            } catch (e: ArrayIndexOutOfBoundsException) { // expected if species group name is on one line.
            }
            // look for the species group name
            if (higherbits.size == nameStartsAt + 1 && higherbits.size > 0) {
                lines = higherbits[nameStartsAt]!!.split("\n".toRegex()).toTypedArray()
                if (lines.size > 1) {
                    val speciesGroupName: Array<String?> = lines[1]!!.trim { it <= ' ' }.split(" +".toRegex()).toTypedArray() // split on one or more spaces
                    // TODO: Test for trinomial flowing onto two lines with authorship on third.
                    try {
                        parseSpeciesGroupName(speciesGroupName)
                        setAuthorship(lines[2]!!.trim { it <= ' ' })
                        setDrawerNumber(lines[3]!!.trim { it <= ' ' })
                    } catch (e: ArrayIndexOutOfBoundsException) { // unexpected, but possible if elements are missing
                    }
                }
            } else if (higherbits.size == 1) { // Handle pathological case of no colon found in higher taxon name string.
//System.out.println(higherbits[0]);
                lines = higherbits[0]!!.split("\n".toRegex()).toTypedArray()
                setFamily(lines[0]!!.trim { it <= ' ' })
                val possFamily: Array<String?> = higherbits[0]!!.trim { it <= ' ' }.split("\n".toRegex()).toTypedArray()
                log!!.debug(possFamily.size + possFamily[0])
                if (possFamily[0]!!.trim { it <= ' ' }.contains(" ")) { // Check for the case of a failed OCR of the separator colon.
// now check.
                    val bits: Array<String?> = possFamily[0]!!.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
                    if (bits.size == 2 && bits[0]!!.length > 4 && bits[1]!!.length > 4) {
                        setFamily(bits[0]!!.trim { it <= ' ' })
                        setSubfamily(bits[1]!!.trim { it <= ' ' })
                    }
                }
                try {
                    setAuthorship(lines[lines.size - 2]!!.trim { it <= ' ' })
                    setDrawerNumber(lines[lines.size - 1]!!.trim { it <= ' ' })
                    if (lines[lines.size - 2]!!.trim { it <= ' ' }.matches(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                        setAuthorship(lines[lines.size - 3]!!.trim { it <= ' ' })
                        setDrawerNumber(lines[lines.size - 2]!!.trim { it <= ' ' })
                    }
                } catch (e: ArrayIndexOutOfBoundsException) { // unexpected, but possible if elements are missing
                    log.debug(e)
                }
                if (lines.size > 1) {
                    try {
                        val speciesGroupName: Array<String?> = lines[lines.size - 3]!!.trim { it <= ' ' }.split(" +".toRegex()).toTypedArray() // split on one or more spaces
                        parseSpeciesGroupName(speciesGroupName)
                    } catch (e: ArrayIndexOutOfBoundsException) { // unexpected, but possible if elements are missing
                        log.debug("Missing element in: " + higherbits[0]!!.replace("\n", ":"))
                    }
                    // TODO: Test for trinomial flowing onto two lines with authorship on third.
                }
            } // higherbits length == 1
            // recheck patterns in parse
            if (lines != null) {
                var drawernumberOnLine = -1
                var authorshipOnLine = -1
                for (i in lines.indices) {
                    if (lines[i]!!.trim { it <= ' ' }.matches(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                        drawernumberOnLine = i
                    }
                    //   \(?[A-Za-z& ]*,[0-9]{4}\)?
                    if (lines[i]!!.trim { it <= ' ' }.matches("\\(?[A-Za-z& ]*, [0-9]{4}\\)?")) {
                        authorshipOnLine = i
                    }
                }
                if (authorshipOnLine > 0 && authorship != lines[authorshipOnLine]!!.trim { it <= ' ' }) {
                    setAuthorship(lines[authorshipOnLine]!!.trim { it <= ' ' })
                }
                if (drawernumberOnLine > 0 && getDrawerNumber() != lines[drawernumberOnLine]!!.trim { it <= ' ' }) {
                    setDrawerNumber(lines[drawernumberOnLine]!!.trim { it <= ' ' })
                }
            }
        } // text is not null
    }

    /** given a species group name in a string array, parses the component genus, species,
     * subspecies, infraspecific, and infraspecific rank parts.
     * @param speciesGroupName the species group name to parse.
     */
    private fun parseSpeciesGroupName(speciesGroupName: Array<String?>?) {
        if (speciesGroupName!![0]!!.length == 1) { // Probably an incorrect OCR inserting a space after the first letter of the Generic name
// Handle as special case by moving everything over by 1.
            speciesGroupName[0] = speciesGroupName[0] + speciesGroupName[1]
            if (speciesGroupName.size > 2) {
                speciesGroupName[1] = speciesGroupName[2]
                if (speciesGroupName.size > 3) {
                    for (i in 3 until speciesGroupName.size) {
                        speciesGroupName[i - 1] = speciesGroupName[i]
                        speciesGroupName[i] = ""
                    }
                } else {
                    speciesGroupName[2] = ""
                }
            }
        }
        setGenus(speciesGroupName[0]!!)
        if (speciesGroupName.size > 1) {
            setSpecificEpithet(speciesGroupName[1]!!)
            if (speciesGroupName.size > 2) {
                if (speciesGroupName[2]!!.matches("^var.|forma|f.|form")) {
                    try {
                        setInfraspecificEpithet(speciesGroupName[3]!!)
                        setInfraspecificRank(speciesGroupName[2]!!)
                    } catch (e: ArrayIndexOutOfBoundsException) { // Unexpected error found infrasubspecific indicator with no epithet
// following it.
                        log!!.error("Parsing error: found infrasubspecific rank with no epithet in: " + speciesGroupName.toString().trim { it <= ' ' })
                    }
                } else {
                    try {
                        setSubspecificEpithet(speciesGroupName[2]!!)
                    } catch (e: ArrayIndexOutOfBoundsException) { //expected for species
                    }
                }
            } // speciesgroupname .length > 2
        } // speciesgroupname .length > 1
    }

    /**
     * @param authorship the authorship to set
     */
    protected fun setAuthorship(authorship: String) {
        this.authorship = authorship.trim { it <= ' ' }
    }

    /**
     * @param family the family to set
     */
    protected fun setFamily(family: String) {
        this.family = family.replace('\n', ' ').trim { it <= ' ' }.replace('0', 'o').replace("\\s".toRegex(), "").replace("^1".toRegex(), "")
        // Truncate to database field size.
// Truncating family is probably the most critical, as bad OCR where colons aren't read
// will end up here.
        if (this.family!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Family")) {
            this.family = this.family!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Family"))
        }
    }

    /**
     * @param subfamily the subfamily to set
     */
    protected fun setSubfamily(subfamily: String) {
        this.subfamily = subfamily.replace('\n', ' ').trim { it <= ' ' }.replace('0', 'o').replace("\\s".toRegex(), "")
        if (this.subfamily!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Subfamily")) {
            this.subfamily = this.subfamily!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Subfamily"))
        }
    }

    /**
     * @param tribe the tribe to set
     */
    protected fun setTribe(tribe: String) {
        this.tribe = tribe.replace('\n', ' ').trim { it <= ' ' }.replace('0', 'o')
        if (this.tribe!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Tribe")) {
            this.tribe = this.tribe!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Tribe"))
        }
    }

    /**
     * @param genus the genus to set
     */
    protected fun setGenus(genus: String) { // strip off leading/trailing whitespace
// strip off a leading [
        this.genus = genus.trim { it <= ' ' }.replaceFirst("^\\[".toRegex(), "")
    }

    /**
     * @param specificEpithet the specificEpithet to set
     */
    protected fun setSpecificEpithet(specificEpithet: String) {
        this.specificEpithet = specificEpithet.trim { it <= ' ' }
    }

    /**
     * @param subspecificEpithet the subspecificEpithet to set
     */
    protected fun setSubspecificEpithet(subspecificEpithet: String) {
        this.subspecificEpithet = subspecificEpithet.trim { it <= ' ' }
    }

    /**
     * @param infraspecificEpithet the infraspecificEpithet to set
     */
    protected fun setInfraspecificEpithet(infraspecificEpithet: String) {
        this.infraspecificEpithet = infraspecificEpithet.trim { it <= ' ' }
    }

    /**
     * @param infraspecificRank the infraspecificRank to set
     */
    protected fun setInfraspecificRank(infraspecificRank: String) {
        this.infraspecificRank = infraspecificRank.replace('\n', ' ').trim { it <= ' ' }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner#getDrawerNumber()
     */
    override fun getDrawerNumber(): String {
        return drawerNumber!!.trim { it <= ' ' }
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    protected fun setDrawerNumber(drawerNumber: String) {
        this.drawerNumber = drawerNumber.replace('\n', ' ').trim { it <= ' ' }
    }

    companion object {
        private val log = LogFactory.getLog(UnitTrayLabelParser::class.java)
    }

    /**
     * Create a unit tray label parser and parse the text.  Call the get methods of the
     * instance to return the parsed text.
     *
     * Usage:
     * `
     * Specimen s = new Specimen();
     * UnitTrayLabelParser p = new UnitTrayLabelParser(aStringFromOCR);
     * s.setFamily(p.Family);
    ` *
     *
     * @param aStringToParse
     */
    init {
        log!!.debug(text)
        var done = false
        //allie edit
        if (text != null && text!!.endsWith("}") && text!!.startsWith("{")) {
            if (parseFromJSON(text!!)) {
                done = true
            }
        }
        if (!done) {
            parse()
        }
    }
}
