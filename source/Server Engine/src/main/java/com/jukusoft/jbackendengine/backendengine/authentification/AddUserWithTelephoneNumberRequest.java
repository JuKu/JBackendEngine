package com.jukusoft.jbackendengine.backendengine.authentification;

/**
 * Created by Justin on 02.07.2015.
 */
public class AddUserWithTelephoneNumberRequest {

    private String telephoneNumber = "";
    private String passwordHash = "";
    private String countryCode = "";
    private int buildNumer = 2;
    private String os = "";

    public AddUserWithTelephoneNumberRequest (String telephoneNumber, String passwordHash, String countryCode, int buildNumber) {
        this.telephoneNumber = telephoneNumber;
        this.passwordHash = passwordHash;
        this.countryCode = countryCode;
        this.buildNumer = buildNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getBuildNumer() {
        return buildNumer;
    }

    public String getOS () {
        return this.os;
    }

    public void setOS (String os) {
        this.os = os;
    }

}
