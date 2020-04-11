/**
 * LatLong.java
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
package edu.harvard.mcz.imagecapture.entity


import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * The persistent class for the LAT_LONG database table.
 */
class LatLong : Serializable, Cloneable {
    var latLongId: Long? = null
    var acceptedLatLongFg: Boolean? = true
    var datum: String? = null
        get() = if (field == null) "" else field
    private var decLat: BigDecimal? = null
    private var decLatMin: BigDecimal? = null
    private var decLong: BigDecimal? = null
    private var decLongMin: BigDecimal? = null
    var determinedByAgent: String? = null
    var determinedDate: Date? = Date()
    private var extent: BigDecimal? = null
    var fieldVerifiedFg: Boolean? = null
    var georefmethod: String? = ""
    private var gpsaccuracy: BigDecimal? = null
    var latDeg: Int? = null
    var latDir: String? = null
    var latLongForNnpFg: Boolean? = null
    var latLongRefSource: String? = "unknown"
    var latLongRemarks: String? = null
    var latMin: Int? = null
    private var latSec: BigDecimal? = null
    var longDeg: Int? = null
    var longDir: String? = null
    var longMin: Int? = null
    private var longSec: BigDecimal? = null
    var maxErrorDistance: Int? = null
    private var maxErrorUnits: String? = null
    var nearestNamedPlace: String? = null
    var origLatLongUnits: String? = "decimal degrees"
    /* Valid values for origLatLongUnits:
        decimal degrees
        deg. min. sec.
        degrees dec. minutes
        unknown
                 */
    var utmEw: Int? = null
    var utmNs: Int? = null
    var utmZone: String? = null
    var verificationstatus: String? = "unknown"
    private var specimenId: Specimen? = null
    val isEmpty: Boolean
        get() = equalsOneD(this, LatLong())

    fun getDecLat(): BigDecimal? {
        return decLat
    }

    fun setDecLat(decLat: BigDecimal?) {
        this.decLat = decLat
    }

    val decLatString: String?
        get() = if (decLat == null) {
            ""
        } else {
            decLat.toPlainString()
        }

    fun getDecLatMin(): BigDecimal? {
        return decLatMin
    }

    fun setDecLatMin(decLatMin: BigDecimal?) {
        this.decLatMin = decLatMin
    }

    val decLatMinString: String?
        get() = if (decLatMin == null) {
            ""
        } else {
            decLatMin.toPlainString()
        }

    fun getDecLong(): BigDecimal? {
        return decLong
    }

    fun setDecLong(decLong: BigDecimal?) {
        this.decLong = decLong
    }

    val decLongString: String?
        get() = if (decLong == null) {
            ""
        } else {
            decLong.toPlainString()
        }

    fun getDecLongMin(): BigDecimal? {
        return decLongMin
    }

    fun setDecLongMin(decLongMin: BigDecimal?) {
        this.decLongMin = decLongMin
    }

    fun getExtent(): BigDecimal? {
        return extent
    }

    fun setExtent(extent: BigDecimal?) {
        this.extent = extent
    }

    fun getGpsaccuracy(): BigDecimal? {
        return gpsaccuracy
    }

    fun setGpsaccuracy(gpsaccuracy: BigDecimal?) {
        this.gpsaccuracy = gpsaccuracy
    }

    fun getLatSec(): BigDecimal? {
        return latSec
    }

    fun setLatSec(latSec: BigDecimal?) {
        this.latSec = latSec
    }

    fun getLongSec(): BigDecimal? {
        return longSec
    }

    fun setLongSec(longSec: BigDecimal?) {
        this.longSec = longSec
    }

    fun getMaxErrorUnits(): String? {
        if (maxErrorUnits == null || maxErrorUnits!!.length == 0) {
            maxErrorUnits = "m"
        }
        return maxErrorUnits
    }

    fun setMaxErrorUnits(maxErrorUnits: String?) {
        this.maxErrorUnits = maxErrorUnits
    }

    var specimen: 
import edu.harvard.mcz.imagecapture.entity.Specimen?
        get() = specimenId
        set(specimen) {
            specimenId = specimen
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
     * @return
     */
    val latDegString: String
        get() = if (latDeg == null) {
            ""
        } else {
            latDeg.toString()
        }

    /**
     * @return
     */
    val latMinString: String
        get() = if (latMin == null) {
            ""
        } else {
            latMin.toString()
        }

    /**
     * @return
     */
    val latSecString: String?
        get() = if (latSec == null) {
            ""
        } else {
            latSec.toPlainString()
        }

    /**
     * @return
     */
    val longDegString: String
        get() = if (longDeg == null) {
            ""
        } else {
            longDeg.toString()
        }

    /**
     * @return
     */
    val decLongMinString: String?
        get() = if (decLongMin == null) {
            ""
        } else {
            decLongMin.toPlainString()
        }

    /**
     * @return
     */
    val longMinString: String
        get() = if (longMin == null) {
            ""
        } else {
            longMin.toString()
        }

    /**
     * @return
     */
    val longSecString: String?
        get() = if (longSec == null) {
            ""
        } else {
            longSec.toPlainString()
        }

    /**
     * @return
     */
    val maxErrorDistanceString: String
        get() = if (maxErrorDistance == null) {
            ""
        } else {
            maxErrorDistance.toString()
        }

    /**
     * @return
     */
    val gpsaccuracyString: String?
        get() = if (gpsaccuracy == null) {
            ""
        } else {
            gpsaccuracy.toPlainString()
        }

    /**
     * Check whether this object has the same properties as a similar object.
     * Note that due to historical reasons, not every property is compared.
     * If need to check for strict equality, compare the id's!
     * Examples of uncompaired properties:
     *
     * @param coord
     * @return whether these few properties are equal.
     * @see LatLong.equalsOneD
     */
    fun equals(coord: LatLong): Boolean {
        return equalsOneD(coord, this) && equalsOneD(this, coord)
    }

    /**
     * Check whether two objects are similar in the sense where one of the objects
     * Examples of uncompaired properties:
     * - georefmethod: had different defaults over time, so we could get false
     * positives
     * - fieldVerifiedFg: had undefined behaviour in some cases
     * - latLongForNnpFg: had undefined behaviour in some cases
     *
     * @param subject    this object may have null/""/0.0 values to return true
     * @param defaultVal this object has the acceptable default values to return true
     * @return
     */
    private fun equalsOneD(subject: LatLong, defaultVal: LatLong): Boolean {
        return subject.acceptedLatLongFg === defaultVal.acceptedLatLongFg &&
                emptyOrEqual(subject.decLat, defaultVal.decLat) &&
                emptyOrEqual(subject.decLatMin, defaultVal.decLatMin) &&
                emptyOrEqual(subject.decLong, defaultVal.decLong) &&
                emptyOrEqual(subject.decLongMin, defaultVal.decLongMin) &&
                emptyOrEqual(subject.determinedByAgent, defaultVal.determinedByAgent) &&
                emptyOrEqual(subject.extent, defaultVal.extent) &&
                emptyOrEqual(subject.latDeg, defaultVal.latDeg) &&
                emptyOrEqual(subject.latDir, defaultVal.latDir) &&
                emptyOrEqual(subject.latMin, defaultVal.latMin) &&
                emptyOrEqual(subject.latSec, defaultVal.latSec) &&
                emptyOrEqual(subject.gpsaccuracy, defaultVal.gpsaccuracy) &&
                emptyOrEqual(subject.maxErrorDistance, defaultVal.maxErrorDistance)
    }

    private fun emptyOrEqual(subject: String?, defaultVal: String?): Boolean {
        return subject == null || subject == "" || subject == defaultVal
    }

    private fun emptyOrEqual(subject: Number?, defaultVal: Number?): Boolean {
        return subject == null || subject.doubleValue() == 0.0 || subject == defaultVal
    }

    public override fun clone(): LatLong {
        val newgeo = LatLong()
        newgeo.acceptedLatLongFg = acceptedLatLongFg
        newgeo.datum = datum
        newgeo.setDecLat(getDecLat())
        newgeo.setDecLatMin(getDecLatMin())
        newgeo.setDecLong(getDecLong())
        newgeo.setDecLongMin(getDecLongMin())
        newgeo.determinedByAgent = determinedByAgent
        newgeo.determinedDate = determinedDate
        newgeo.setExtent(getExtent())
        newgeo.fieldVerifiedFg = fieldVerifiedFg
        newgeo.georefmethod = georefmethod
        newgeo.setGpsaccuracy(getGpsaccuracy())
        newgeo.latDeg = latDeg
        newgeo.latDir = latDir
        newgeo.latLongForNnpFg = latLongForNnpFg
        newgeo.latLongRefSource = latLongRefSource
        newgeo.latLongRemarks = latLongRemarks
        newgeo.latMin = latMin
        newgeo.setLatSec(getLatSec())
        newgeo.longDeg = longDeg
        newgeo.longDir = longDir
        newgeo.longMin = longMin
        newgeo.setLongSec(getLongSec())
        newgeo.maxErrorDistance = maxErrorDistance
        newgeo.setMaxErrorUnits(getMaxErrorUnits())
        newgeo.nearestNamedPlace = nearestNamedPlace
        newgeo.origLatLongUnits = origLatLongUnits
        newgeo.utmEw = utmEw
        newgeo.utmNs = utmNs
        newgeo.utmZone = utmZone
        newgeo.verificationstatus = verificationstatus
        return newgeo
    }

    companion object {
        private const val serialVersionUID = 1L
        val georefMethodValues: MutableList<String?>
            get() {
                val result: MutableList<String?> = ArrayList()
                result.add("not recorded")
                result.add("unknown")
                result.add("GEOLocate")
                result.add("Google Earth")
                result.add("Gazeteer")
                result.add("GPS")
                result.add("MaNIS/HertNet/ORNIS Georeferencing Guidelines")
                return result
            }

        val datumValues: MutableList<String?>
            get() {
                val result: MutableList<String?> = ArrayList()
                result.add("not recorded")
                result.add("unknown")
                result.add("WGS84")
                result.add("NAD27")
                result.add("WGS 1972")
                result.add("North American 1983")
                result.add("North American 1928")
                result.add("North American 1929")
                result.add("Australian Geodetic 1966")
                result.add("Bogota Observatory")
                result.add("Corrego Alegre")
                result.add("Provisional South American 1956")
                result.add("PRP_M")
                result.add("POS")
                result.add("GRA")
                result.add("GDA94")
                result.add("Fundamental de Ocotepeque")
                result.add("AGD66")
                result.add("Clarke 1958")
                result.add("Japanese Geodetic Datum 2000")
                return result
            }
    }
}
