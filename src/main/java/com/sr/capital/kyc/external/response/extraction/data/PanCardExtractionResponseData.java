package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanCardExtractionResponseData implements Serializable {

    private ExtractionOutput extractionOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExtractionOutput implements Serializable {

        private String age;

        private String dateOfBirth;

        private String dateOfIssue;

        private String fathersName;

        private String idNumber;

        private boolean isScanned;

        private boolean minor;

        private String nameOnCard;

        private String panType;

    }

}
