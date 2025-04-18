/**
 * LatLong.java
 * edu.harvard.mcz.imagecapture.data
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the LAT_LONG database table.
 */
public class LatLong implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private Long latLongId;
    private Boolean acceptedLatLongFg = true;
    private String datum;
    private BigDecimal decLat;
    private BigDecimal decLatMin;
    private BigDecimal decLong;
    private BigDecimal decLongMin;
    private String determinedByAgent;
    private Date determinedDate = new Date();
    private BigDecimal extent;
    private Boolean fieldVerifiedFg;
    private String georefmethod = "";
    private BigDecimal gpsaccuracy;
    private Integer latDeg;
    private String latDir;
    private Boolean latLongForNnpFg;
    private String latLongRefSource = "unknown";
    private String latLongRemarks;
    private Integer latMin;
    private BigDecimal latSec;
    private Integer longDeg;
    private String longDir;
    private Integer longMin;
    private BigDecimal longSec;
    private Integer maxErrorDistance;
    private String maxErrorUnits;
    private String nearestNamedPlace;
    private String origLatLongUnits = "decimal degrees";
    /* Valid values for origLatLongUnits:
        decimal degrees
        deg. min. sec.
        degrees dec. minutes
        unknown
                 */
    private Integer utmEw;
    private Integer utmNs;
    private String utmZone;
    private String verificationstatus = "unknown";

    private Specimen specimenId;

    public LatLong() {
    }

    public static List<String> getGeorefMethodValues() {
        List<String> result = new ArrayList<String>();
        result.add("not recorded");
        result.add("unknown");
        result.add("GEOLocate");
        result.add("Google Earth");
        result.add("Gazeteer");
        result.add("GPS");
        result.add("MaNIS/HertNet/ORNIS Georeferencing Guidelines");
        return result;
    }

    public static List<String> getDatumValues() {
        List<String> result = new ArrayList<String>();
        result.add("not recorded");
        result.add("unknown");
        result.add("WGS84");
        result.add("NAD27");
        result.add("WGS 1972");
        result.add("North American 1983");
        result.add("North American 1928");
        result.add("North American 1929");
        result.add("Australian Geodetic 1966");
        result.add("Bogota Observatory");
        result.add("Corrego Alegre");
        result.add("Provisional South American 1956");
        result.add("PRP_M");
        result.add("POS");
        result.add("GRA");
        result.add("GDA94");
        result.add("Fundamental de Ocotepeque");
        result.add("AGD66");
        result.add("Clarke 1958");
        result.add("Japanese Geodetic Datum 2000");
        return result;
    }

    public boolean isEmpty() {
        return this.equalsOneD(this, new LatLong());
    }

    public Long getLatLongId() {
        return this.latLongId;
    }

    public void setLatLongId(Long latLongId) {
        this.latLongId = latLongId;
    }

    public Boolean getAcceptedLatLongFg() {
        return this.acceptedLatLongFg;
    }

    public void setAcceptedLatLongFg(Boolean acceptedLatLongFg) {
        this.acceptedLatLongFg = acceptedLatLongFg;
    }

    public String getDatum() {
        return this.datum == null ? "" : this.datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public BigDecimal getDecLat() {
        return this.decLat;
    }

    public void setDecLat(BigDecimal decLat) {
        this.decLat = decLat;
    }

    public String getDecLatString() {
        if (decLat == null) {
            return "";
        } else {
            return decLat.toPlainString();
        }
    }

    public BigDecimal getDecLatMin() {
        return this.decLatMin;
    }

    public void setDecLatMin(BigDecimal decLatMin) {
        this.decLatMin = decLatMin;
    }

    public String getDecLatMinString() {
        if (decLatMin == null) {
            return "";
        } else {
            return decLatMin.toPlainString();
        }
    }

    public BigDecimal getDecLong() {
        return this.decLong;
    }

    public void setDecLong(BigDecimal decLong) {
        this.decLong = decLong;
    }

    public String getDecLongString() {
        if (decLong == null) {
            return "";
        } else {
            return decLong.toPlainString();
        }
    }

    public BigDecimal getDecLongMin() {
        return this.decLongMin;
    }

    public void setDecLongMin(BigDecimal decLongMin) {
        this.decLongMin = decLongMin;
    }

    public String getDeterminedByAgent() {
        return this.determinedByAgent;
    }

    public void setDeterminedByAgent(String determinedByAgent) {
        this.determinedByAgent = determinedByAgent;
    }

    public Date getDeterminedDate() {
        return this.determinedDate;
    }

    public void setDeterminedDate(Date determinedDate) {
        this.determinedDate = determinedDate;
    }

    public BigDecimal getExtent() {
        return this.extent;
    }

    public void setExtent(BigDecimal extent) {
        this.extent = extent;
    }

    public Boolean getFieldVerifiedFg() {
        return this.fieldVerifiedFg;
    }

    public void setFieldVerifiedFg(Boolean fieldVerifiedFg) {
        this.fieldVerifiedFg = fieldVerifiedFg;
    }

    public String getGeorefmethod() {
        return this.georefmethod;
    }

    public void setGeorefmethod(String georefmethod) {
        this.georefmethod = georefmethod;
    }

    public BigDecimal getGpsaccuracy() {
        return this.gpsaccuracy;
    }

    public void setGpsaccuracy(BigDecimal gpsaccuracy) {
        this.gpsaccuracy = gpsaccuracy;
    }

    public Integer getLatDeg() {
        return this.latDeg;
    }

    public void setLatDeg(Integer latDeg) {
        this.latDeg = latDeg;
    }

    public String getLatDir() {
        return this.latDir;
    }

    public void setLatDir(String latDir) {
        this.latDir = latDir;
    }

    public Boolean getLatLongForNnpFg() {
        return this.latLongForNnpFg;
    }

    public void setLatLongForNnpFg(Boolean latLongForNnpFg) {
        this.latLongForNnpFg = latLongForNnpFg;
    }

    public String getLatLongRefSource() {
        return this.latLongRefSource;
    }

    public void setLatLongRefSource(String latLongRefSource) {
        this.latLongRefSource = latLongRefSource;
    }

    public String getLatLongRemarks() {
        return this.latLongRemarks;
    }

    public void setLatLongRemarks(String latLongRemarks) {
        this.latLongRemarks = latLongRemarks;
    }

    public Integer getLatMin() {
        return this.latMin;
    }

    public void setLatMin(Integer latMin) {
        this.latMin = latMin;
    }

    public BigDecimal getLatSec() {
        return this.latSec;
    }

    public void setLatSec(BigDecimal latSec) {
        this.latSec = latSec;
    }

    public Integer getLongDeg() {
        return this.longDeg;
    }

    public void setLongDeg(Integer longDeg) {
        this.longDeg = longDeg;
    }

    public String getLongDir() {
        return this.longDir;
    }

    public void setLongDir(String longDir) {
        this.longDir = longDir;
    }

    public Integer getLongMin() {
        return this.longMin;
    }

    public void setLongMin(Integer longMin) {
        this.longMin = longMin;
    }

    public BigDecimal getLongSec() {
        return this.longSec;
    }

    public void setLongSec(BigDecimal longSec) {
        this.longSec = longSec;
    }

    public Integer getMaxErrorDistance() {
        return this.maxErrorDistance;
    }

    public void setMaxErrorDistance(Integer maxErrorDistance) {
        this.maxErrorDistance = maxErrorDistance;
    }

    public String getMaxErrorUnits() {
        if (maxErrorUnits == null || maxErrorUnits.length() == 0) {
            maxErrorUnits = "m";
        }
        return this.maxErrorUnits;
    }

    public String getActualMaxErrorUnits() {
        return this.maxErrorUnits;
    }

    public void setMaxErrorUnits(String maxErrorUnits) {
        this.maxErrorUnits = maxErrorUnits;
    }

    public String getNearestNamedPlace() {
        return this.nearestNamedPlace;
    }

    public void setNearestNamedPlace(String nearestNamedPlace) {
        this.nearestNamedPlace = nearestNamedPlace;
    }

    public String getOrigLatLongUnits() {
        return this.origLatLongUnits;
    }

    public void setOrigLatLongUnits(String origLatLongUnits) {
        this.origLatLongUnits = origLatLongUnits;
    }

    public Specimen getSpecimen() {
        return this.specimenId;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimenId = specimen;
    }

    // legacy as long as the hibernate property is called specimenId
    public Specimen getSpecimenId() {
        return specimenId;
    }

    // legacy as long as the hibernate property is called specimenId
    public void setSpecimenId(Specimen specimenId) {
        this.specimenId = specimenId;
    }

    public Integer getUtmEw() {
        return this.utmEw;
    }

    public void setUtmEw(Integer utmEw) {
        this.utmEw = utmEw;
    }

    public Integer getUtmNs() {
        return this.utmNs;
    }

    public void setUtmNs(Integer utmNs) {
        this.utmNs = utmNs;
    }

    public String getUtmZone() {
        return this.utmZone;
    }

    public void setUtmZone(String utmZone) {
        this.utmZone = utmZone;
    }

    public String getVerificationstatus() {
        return this.verificationstatus;
    }

    public void setVerificationstatus(String verificationstatus) {
        this.verificationstatus = verificationstatus;
    }

    /**
     * @return
     */
    public String getLatDegString() {
        if (this.latDeg == null) {
            return "";
        } else {
            return latDeg.toString();
        }
    }

    /**
     * @return
     */
    public String getLatMinString() {
        if (this.latMin == null) {
            return "";
        } else {
            return latMin.toString();
        }
    }

    /**
     * @return
     */
    public String getLatSecString() {
        if (this.latSec == null) {
            return "";
        } else {
            return latSec.toPlainString();
        }
    }

    /**
     * @return
     */
    public String getLongDegString() {
        if (this.longDeg == null) {
            return "";
        } else {
            return longDeg.toString();
        }
    }

    /**
     * @return
     */
    public String getDecLongMinString() {
        if (this.decLongMin == null) {
            return "";
        } else {
            return decLongMin.toPlainString();
        }
    }

    /**
     * @return
     */
    public String getLongMinString() {
        if (this.longMin == null) {
            return "";
        } else {
            return longMin.toString();
        }
    }

    /**
     * @return
     */
    public String getLongSecString() {
        if (this.longSec == null) {
            return "";
        } else {
            return longSec.toPlainString();
        }
    }

    /**
     * @return
     */
    public String getMaxErrorDistanceString() {
        if (this.maxErrorDistance == null) {
            return "";
        } else {
            return maxErrorDistance.toString();
        }
    }

    /**
     * @return
     */
    public String getGpsaccuracyString() {
        if (this.gpsaccuracy == null) {
            return "";
        } else {
            return gpsaccuracy.toPlainString();
        }
    }

    /**
     * Check whether this object has the same properties as a similar object.
     * Note that due to historical reasons, not every property is compared.
     * If need to check for strict equality, compare the id's!
     * Examples of uncompaired properties:
     *
     * @param coord
     * @return whether these few properties are equal.
     * @see LatLong#equalsOneD(LatLong, LatLong)
     * TODO: not used, but if, please refactor.
     */
    public boolean equals(LatLong coord) {
        return this.equalsOneD(coord, this) && this.equalsOneD(this, coord);
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
    private boolean equalsOneD(LatLong subject, LatLong defaultVal) {
        return subject.acceptedLatLongFg == defaultVal.acceptedLatLongFg &&
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
                emptyOrEqual(subject.maxErrorDistance, defaultVal.maxErrorDistance);
    }

    private boolean emptyOrEqual(String subject, String defaultVal) {
        return subject == null || subject.equals("") || subject.equals(defaultVal);
    }

    private boolean emptyOrEqual(java.lang.Number subject, java.lang.Number defaultVal) {
        return subject == null || subject.doubleValue() == 0.0 || subject.equals(defaultVal);
    }

    public LatLong clone() {
        LatLong newgeo = new LatLong();
        newgeo.setAcceptedLatLongFg(this.getAcceptedLatLongFg());
        newgeo.setDatum(this.getDatum());
        newgeo.setDecLat(this.getDecLat());
        newgeo.setDecLatMin(this.getDecLatMin());
        newgeo.setDecLong(this.getDecLong());
        newgeo.setDecLongMin(this.getDecLongMin());
        newgeo.setDeterminedByAgent(this.getDeterminedByAgent());
        newgeo.setDeterminedDate(this.getDeterminedDate());
        newgeo.setExtent(this.getExtent());
        newgeo.setFieldVerifiedFg(this.getFieldVerifiedFg());
        newgeo.setGeorefmethod(this.getGeorefmethod());
        newgeo.setGpsaccuracy(this.getGpsaccuracy());
        newgeo.setLatDeg(this.getLatDeg());
        newgeo.setLatDir(this.getLatDir());
        newgeo.setLatLongForNnpFg(this.getLatLongForNnpFg());
        newgeo.setLatLongRefSource(this.getLatLongRefSource());
        newgeo.setLatLongRemarks(this.getLatLongRemarks());
        newgeo.setLatMin(this.getLatMin());
        newgeo.setLatSec(this.getLatSec());
        newgeo.setLongDeg(this.getLongDeg());
        newgeo.setLongDir(this.getLongDir());
        newgeo.setLongMin(this.getLongMin());
        newgeo.setLongSec(this.getLongSec());
        newgeo.setMaxErrorDistance(this.getMaxErrorDistance());
        newgeo.setMaxErrorUnits(this.getActualMaxErrorUnits());
        newgeo.setNearestNamedPlace(this.getNearestNamedPlace());
        newgeo.setOrigLatLongUnits(this.getOrigLatLongUnits());
        newgeo.setUtmEw(this.getUtmEw());
        newgeo.setUtmNs(this.getUtmNs());
        newgeo.setUtmZone(this.getUtmZone());
        newgeo.setVerificationstatus(this.getVerificationstatus());
        return newgeo;
    }
}
