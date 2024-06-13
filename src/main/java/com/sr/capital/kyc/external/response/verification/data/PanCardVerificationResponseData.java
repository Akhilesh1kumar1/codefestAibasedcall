package com.sr.capital.kyc.external.response.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanCardVerificationResponseData implements Serializable {

    private SourceOutput sourceOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SourceOutput implements Serializable {

        private boolean aadhaarSeedingStatus;

        private String firstName;

        private String gender;

        private String idNumber;

        private String lastName;

        private String middleName;

        private String nameOnCard;

        private String source;

        private String status;

    }

}
