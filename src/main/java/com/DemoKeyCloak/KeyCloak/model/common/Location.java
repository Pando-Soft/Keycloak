package com.DemoKeyCloak.KeyCloak.model.common;

import java.util.UUID;

public class Location {
    private UUID uuid;
    private String country;
    private String countryCode;
    private String city;
    private String addressName;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String notes;
    private String state;
    private String isoCountryCode;

    public UUID getUuid() {
        return this.uuid;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCity() {
        return this.city;
    }

    public String getAddressName() {
        return this.addressName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getState() {
        return this.state;
    }

    public String getIsoCountryCode() {
        return this.isoCountryCode;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setAddressName(final String addressName) {
        this.addressName = addressName;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public void setIsoCountryCode(final String isoCountryCode) {
        this.isoCountryCode = isoCountryCode;
    }

    public Location() {
    }
}
