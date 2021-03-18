package edu.harvard.mcz.imagecapture.entity;

import java.io.Serializable;

public class ISO3166 implements Serializable, Cloneable {
    private static final long serialVersionUID = 3166L;
    private Long id;
    private String countryName;
    private String isoCode;

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ISO3166() {}
}
