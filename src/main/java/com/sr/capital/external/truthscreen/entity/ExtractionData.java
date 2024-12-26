package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omunify.encryption.algorithm.AES256;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractionData {

    @JsonProperty("aadhaar_linked")
    private boolean aadhaarLinked;

    private Address address;

    private String category;

    @JsonProperty("client_id")
    private String clientId;

    private String dob;

    @JsonProperty("dob_check")
    private boolean dobCheck;

    @JsonProperty("dob_verified")
    private boolean dobVerified;

    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("full_name_split")
    private List<String> fullNameSplit;

    private String gender;

    @JsonProperty("input_dob")
    private String inputDob;

    @JsonProperty("less_info")
    private boolean lessInfo;

    @JsonProperty("masked_aadhaar")
    private String maskedAadhaar;

    @JsonProperty("pan_number")
    private String panNumber;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String status;

    public static void encryptData(ExtractionData data, AES256 aes256){
        Address.encryptAddress(data.getAddress(),aes256);
        data.setCategory(aes256.encrypt(data.getCategory()));
        data.setDob(aes256.encrypt(data.getDob()));
        data.setClientId(aes256.encrypt(data.getClientId()));
        data.setEmail(aes256.encrypt(data.getEmail()));
        data.setFullName(aes256.encrypt(data.getFullName()));
        data.setPanNumber(aes256.encrypt(data.getPanNumber()));
        data.setPhoneNumber(aes256.encrypt(data.getPhoneNumber()));
    }

    public static void decryptData(ExtractionData data, AES256 aes256){
        Address.decryptAddress(data.getAddress(),aes256);
        data.setCategory(aes256.decrypt(data.getCategory()));
        data.setDob(aes256.decrypt(data.getDob()));
        data.setClientId(aes256.decrypt(data.getClientId()));
        data.setEmail(aes256.decrypt(data.getEmail()));
        data.setFullName(aes256.decrypt(data.getFullName()));
        data.setPanNumber(aes256.decrypt(data.getPanNumber()));
        data.setPhoneNumber(aes256.decrypt(data.getPhoneNumber()));
    }

}