package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GstExtractionResponseData implements Serializable {

    String statusCd;
    /*private ExtractionOutput extractionOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExtractionOutput implements Serializable {

        private String address;

        private String constitutionOfBusiness;

        private String dateOfLiability;

        private String gstin;

        private boolean isProvisional;

        private String legalName;

        private String panNumber;

        private String tradeName;

        private String typeOfRegistration;

        private String validUpto;

    }*/

}
