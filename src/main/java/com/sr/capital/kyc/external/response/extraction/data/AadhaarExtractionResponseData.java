package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AadhaarExtractionResponseData implements Serializable {

    private ExtractionOutput extractionOutput;

    private QrOutput qrOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExtractionOutput implements Serializable {

        private String address;

        private String dateOfBirth;

        private String district;

        private String fathersName;

        private String gender;

        private String houseNumber;

        private String idNumber;

        private String isScanned;

        private String nameOnCard;

        private String pincode;

        private String state;

        private String streetAddress;

        private String yearOfBirth;

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class QrOutput implements Serializable {

        private String address;

        private String dateOfBirth;

        private String district;

        private String gender;

        private String houseNumber;

        private String idNumber;

        private String nameOnCard;

        private String pincode;

        private String state;

        private String streetAddress;

        private String yearOfBirth;

    }

}
