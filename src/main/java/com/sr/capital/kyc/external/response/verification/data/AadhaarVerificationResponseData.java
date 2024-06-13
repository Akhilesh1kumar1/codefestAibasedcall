package com.sr.capital.kyc.external.response.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AadhaarVerificationResponseData implements Serializable {

    private SourceOutput sourceOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SourceOutput implements Serializable {

        private AgeBandData ageBand;

        private String gender;

        private String mobileNumber;

        private String state;

        private String status;

    }

}
