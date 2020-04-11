package edu.harvard.mcz.imagecapture.entity


import edu.harvard.mcz.imagecapture.entity.SpecimenPart
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycle
import org.apache.commons.logging.LogFactory
import java.util.*

/**
 * Proxy object for SpecimenPart
 */
class SpecimenPart : Cloneable, Cloneable {
    /**
     * @return the specimenPartId
     */
    /**
     * @param specimenPartId the specimenPartId to set
     */
    var specimenPartId: Long? = null
    private var specimenId: Specimen? = null
    /**
     * @return the partName
     */
    /**
     * @param partName the partName to set
     */
    var partName: String? = "whole animal"
    /**
     * @return the preserveMethod
     */
    /**
     * @param preserveMethod the preserveMethod to set
     */
    var preserveMethod: String? = "pinned"
    /**
     * @return the lotCount
     */
    /**
     * @param lotCount the lotCount to set
     */
    var lotCount = 1 // Coll_Object.lot_count
    /**
     * @return the lotCountModifier
     */
    /**
     * @param lotCountModifier the lotCountModifier to set
     */
    var lotCountModifier // Coll_Object.lot_count_modifier
            : String? = null
    private var attributeCollection: MutableCollection<SpecimenPartAttribute?>? = null

    constructor() {}
    /**
     * @param specimenPartId
     * @param specimenId
     * @param partName
     * @param preserveMethod
     * @param lotCount
     * @param lotCountModifier
     * @param attributeCollection
     */
    constructor(specimenPartId: Long?, specimenId: Specimen?,
                partName: String?, preserveMethod: String?, lotCount: Int,
                lotCountModifier: String?,
                attributeCollection: MutableSet<SpecimenPartAttribute?>?) : this(specimenId, partName, preserveMethod, lotCount, lotCountModifier, attributeCollection) {
        this.specimenPartId = specimenPartId
    }

    /**
     * @param specimenId
     * @param partName
     * @param preserveMethod
     * @param lotCount
     * @param lotCountModifier
     * @param attributeCollection
     */
    constructor(specimenId: Specimen?,
                partName: String?, preserveMethod: String?, lotCount: Int,
                lotCountModifier: String?,
                attributeCollection: MutableSet<SpecimenPartAttribute?>?) : super() {
        this.specimenId = specimenId
        this.partName = partName
        this.preserveMethod = preserveMethod
        this.lotCount = lotCount
        this.lotCountModifier = lotCountModifier
        this.attributeCollection = attributeCollection
    }

    /**
     * @return the specimenId
     */
    /**
     * @param specimenId the specimenId to set
     */
    var specimen: 
import edu.harvard.mcz.imagecapture.entity.Specimen?
        get() = specimenId
        set(specimenId) {
            this.specimenId = specimenId
        }

    // legacy as long as the hibernate property is called specimenId
    fun getSpecimenId(): Specimen? {
        return specimenId
    }

    // legacy as long as the hibernate property is called specimenId
    fun setSpecimenId(specimenId: Specimen?) {
        this.specimenId = specimenId
    }

    /**
     * @return the attributeCollection
     */
    fun getAttributeCollection(): MutableCollection<SpecimenPartAttribute?>? {
        if (attributeCollection == null) {
            attributeCollection = HashSet<SpecimenPartAttribute?>()
            val spals = SpecimenPartAttributeLifeCycle()
            attributeCollection.addAll(spals.findBySpecimenPart(this))
        }
        return attributeCollection
    }

    /**
     * @param attributeCollection the attributeCollection to set
     */
    fun setAttributeCollection(
            attributeCollection: MutableCollection<SpecimenPartAttribute?>?) {
        this.attributeCollection = attributeCollection
    }

    var specimenPartAttributes: MutableCollection<
import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute?>?
        get() = getAttributeCollection()
        set(attributeCollection) {
            setAttributeCollection(attributeCollection)
        }

    /**
     * Obtain human readable list of attribute types and values for the
     * specimen part attributes associated with this specimen part.
     *
     * @return string containing concatenated list of attribute types, values, and units.
     * If attribute collection is empty, returns an empty string.
     */
    val partAttributeValuesConcat: String
        get() {
            val result = StringBuffer()
            val i: MutableIterator<SpecimenPartAttribute?> = getAttributeCollection()!!.iterator()
            var counter = 0
            while (i.hasNext()) {
                val attribute: SpecimenPartAttribute? = i.next()
                if (counter > 0) {
                    result.append(", ")
                }
                result.append(attribute.getAttributeType()).append(':').append(attribute.getAttributeValue())
                if (attribute.getAttributeUnits() != null) {
                    result.append(attribute.getAttributeUnits())
                }
                counter++
                log!!.debug(counter)
            }
            return result.toString()
        }

    public override fun clone(): Any {
        val newPart = SpecimenPart(specimenId, partName, preserveMethod, lotCount, lotCountModifier, HashSet<SpecimenPartAttribute?>())
        val newAttributeCollection: MutableSet<SpecimenPartAttribute?> = HashSet<SpecimenPartAttribute?>()
        if (attributeCollection != null) {
            val iterator: MutableIterator<SpecimenPartAttribute?> = attributeCollection!!.iterator()
            while (iterator.hasNext()) {
                val newSpecPartAttr: SpecimenPartAttribute = (iterator.next()!!.clone() as SpecimenPartAttribute)!!
                newSpecPartAttr.setSpecimenPart(newPart)
                newAttributeCollection.add(newSpecPartAttr)
            }
        }
        newPart.attributeCollection = newAttributeCollection
        return newPart
    }

    companion object {
        val PART_NAMES: Array<String?>? = arrayOf(
                "whole animal", "partial animal",
                "partial animal: abdomen", "partial animal: body", "partial animal: legs", "partial animal: wings",
                "cocoon", "frass", "frass chain", "genitalia", "head capsule",
                "head capsule hat", "larval case", "larval shelter",
                "molt", "other", "pupal exuvia", "pupal shelter", "puparium",
                "sphragis"
        )
        val PRESERVATION_NAMES: Array<String?>? = arrayOf("pinned", "pointed", "carded", "envelope", "capsule", "alcohol")
        private val log = LogFactory.getLog(SpecimenPart::class.java)
    }
}
