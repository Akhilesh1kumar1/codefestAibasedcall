package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omunify.encryption.algorithm.AES256;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    private String city;
    private String country;
    private String full;
    private String line1;
    private String line2;
    private String state;

    @JsonProperty("street_name")
    private String streetName;

    private String zip;

    public static void encryptAddress(Address address, AES256 aes256) {
        address.setCity(aes256.encrypt(address.getCity()));
        address.setFull(aes256.encrypt(address.getFull()));
        address.setCountry(aes256.encrypt(address.getCountry()));
        address.setState(aes256.encrypt(address.getState()));
        address.setZip(aes256.encrypt(address.getZip()));
        address.setLine1(aes256.encrypt(address.getLine1()));
        address.setLine2(aes256.encrypt(address.getLine2()));
        address.setStreetName(aes256.encrypt(address.streetName));
    }

    public static void decryptAddress(Address address, AES256 aes256) {
        address.setCity(aes256.decrypt(address.getCity()));
        address.setFull(aes256.decrypt(address.getFull()));
        address.setCountry(aes256.decrypt(address.getCountry()));
        address.setState(aes256.decrypt(address.getState()));
        address.setZip(aes256.decrypt(address.getZip()));
        address.setLine1(aes256.decrypt(address.getLine1()));
        address.setLine2(aes256.decrypt(address.getLine2()));
        address.setStreetName(aes256.decrypt(address.getStreetName())); // Assuming streetName has a getter.
    }
}