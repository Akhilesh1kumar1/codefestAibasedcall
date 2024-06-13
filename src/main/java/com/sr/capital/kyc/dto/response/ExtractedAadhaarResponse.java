package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtractedAadhaarResponse {

    @JsonProperty("id_number")
    private String idNumber;

    @JsonProperty("name")
    private String name;

    @JsonProperty("fathers_name")
    private String fathersName;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("year_of_birth")
    private String yearOfBirth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("house_number")
    private String houseNumber;

    @JsonProperty("street_address")
    private String streetAddress;

    @JsonProperty("address")
    private String address;

    @JsonProperty("district")
    private String district;

    @JsonProperty("state")
    private String state;

    @JsonProperty("pincode")
    private String pincode;

}
