package com.sr.capital.external.truthscreen.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TruthScreenPanComprehensiveDecryptedResponse extends TruthScreenBaseResponse {

    private DataDto data;
    private String message;
    private String messageCode;
    private String status;
    private int statusCode;
    private boolean success;
    @JsonProperty("tsTransID")
    private String tsTransID;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataDto {

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

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressDto {

        private String city;
        private String country;
        private String full;
        private String line1;
        private String line2;
        private String state;

        @JsonProperty("street_name")
        private String streetName;

        private String zip;
    }


}
