package edu.harvard.mcz.imagecapture.entity;

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * Tracking generated by hbm2java
 */
public class Tracking implements java.io.Serializable {

    private static final long serialVersionUID = 3045144335474980280L;

    private Long trackingId;
    private Specimen specimen;
    private String user;
    private String eventType;
    private Date eventDateTime;

    /**
     * Utility constructor if no date is needed
     */
    public Tracking() {

    }

    public Tracking(Specimen specimen, Date eventDateTime) {
        this.specimen = specimen;
        if (eventDateTime == null) {
            this.eventDateTime = new Date();
        } else {
            this.eventDateTime = (Date) eventDateTime.clone();
        }
    }

    public Tracking(Specimen specimen, String user, String eventType) {
        this(specimen, user, eventType, null);
    }

    public Tracking(Specimen specimen, String user, String eventType,
                    Date eventDateTime) {
        this(specimen, eventDateTime);
        this.user = user;
        this.eventType = eventType;
    }

    public Long getTrackingId() {
        return this.trackingId;
    }

    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }

    public Specimen getSpecimen() {
        return this.specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getEventDateTime() {
        Date result = this.eventDateTime;
        return result;
    }

    public void setEventDateTime(Date eventDateTime) {
        if (eventDateTime == null) {
            this.eventDateTime = null;
        } else {
            this.eventDateTime = (Date) eventDateTime.clone();
        }
    }

}