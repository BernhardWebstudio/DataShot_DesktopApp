package edu.harvard.mcz.imagecapture.entity


import edu.harvard.mcz.imagecapture.entity.SpecimenPart
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.util.*

/**
 * Proxy object for SpecimenPartAttribute
 */
class SpecimenPartAttribute : Cloneable, Cloneable {
    /**
     * @return the specimenPartAttributeId
     */
    /**
     * @param specimenPartAttributeId the specimenPartAttributeId to set
     */
    var specimenPartAttributeId: Long? = null
    private var specimenPartId: SpecimenPart? = null
    /**
     * @return the attributeType
     */
    /**
     * @param attributeType the attributeType to set
     */
    var attributeType: String? = "caste"
    /**
     * @return the attributeValue
     */
    /**
     * @param attributeValue the attributeValue to set
     */
    var attributeValue: String? = null
    /**
     * @return the attributeUnits
     */
    /**
     * @param attributeUnits the attributeUnits to set
     */
    var attributeUnits: String? = ""
    /**
     * @return the attributeRemark
     */
    /**
     * @param attributeRemark the attributeRemark to set
     */
    var attributeRemark: String? = null
    /**
     * @return the attributeDeterminer
     */
    /**
     * @param attributeDeterminer the attributeDeterminer to set
     */
    var attributeDeterminer: String? = null
    /**
     * @return the prepTypeAttributeDate
     */
    /**
     * @param prepTypeAttributeDate the prepTypeAttributeDate to set
     */
    var attributeDate: Date? = null

    /**
     * @param specimenPartAttributeId
     * @param specimenPartId
     * @param attributeType
     * @param attributeValue
     * @param attributeUnits
     * @param attributeRemark
     * @param attributeDeterminer
     * @param attributeDate
     */
    constructor(specimenPartAttributeId: Long?,
                specimenPartId: SpecimenPart?, attributeType: String?,
                attributeValue: String?, attributeUnits: String?,
                attributeRemark: String?, attributeDeterminer: String?,
                attributeDate: Date?) : this(specimenPartId, attributeType, attributeValue, attributeUnits, attributeRemark, attributeDeterminer, attributeDate) {
        this.specimenPartAttributeId = specimenPartAttributeId
    }

    /**
     * @param specimenPartId
     * @param attributeType
     * @param attributeValue
     * @param attributeUnits
     * @param attributeRemark
     * @param attributeDeterminer
     * @param attributeDate
     */
    constructor(specimenPartId: SpecimenPart?, attributeType: String?,
                attributeValue: String?, attributeUnits: String?,
                attributeRemark: String?, attributeDeterminer: String?,
                attributeDate: Date?) : super() {
        this.specimenPartId = specimenPartId
        this.attributeType = attributeType
        this.attributeValue = attributeValue
        this.attributeUnits = attributeUnits
        this.attributeRemark = attributeRemark
        this.attributeDeterminer = attributeDeterminer
        this.attributeDate = attributeDate
    }

    /**
     *
     */
    constructor() { // TODO Auto-generated constructor stub
    }

    /**
     * @return the specimenPartId
     */
    /**
     * @param specimenPart the specimenPartId to set
     */
    var specimenPart: 
import edu.harvard.mcz.imagecapture.entity.SpecimenPart?
        get() = specimenPartId
        set(specimenPart) {
            specimenPartId = specimenPart
        }

    // legacy as long as the hibernate property is called specimenPartId
    fun getSpecimenPartId(): SpecimenPart? {
        return specimenPartId
    }

    // legacy as long as the hibernate property is called specimenId
    fun setSpecimenPartId(specimenPartId: SpecimenPart?) {
        this.specimenPartId = specimenPartId
    }

    public override fun clone(): Any {
        return SpecimenPartAttribute(
                specimenPartId!!.clone() as SpecimenPart, attributeType, attributeValue, attributeUnits, attributeRemark, attributeDeterminer, attributeDate!!.clone() as Date
        )
    }

    companion object {
        private val log: Log = LogFactory.getLog(SpecimenPart::class.java)
    }
}
