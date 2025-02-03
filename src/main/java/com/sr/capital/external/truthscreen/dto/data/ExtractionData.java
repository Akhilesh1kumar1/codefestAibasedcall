package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractionData {

    @JsonProperty("aadhaar_linked")
    private boolean aadhaarLinked;

    private AddressDto address;

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
}

