package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankExtractionResponseData implements Serializable {

    private ExtractionOutput extractionOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExtractionOutput implements Serializable {

        private String accountName;

        private String accountNo;

        private String bankAddress;

        private String bankName;

        private String dateOfIssue;

        private String ifscCode;

        private boolean isScanned;

        private String micrChequeNumber;

        private String micrCode;

    }

}
