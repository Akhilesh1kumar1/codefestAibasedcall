package com.sr.capital.kyc.external.response.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanGstCrossVerificationResponseData implements Serializable {

    private SourceOutput sourceOutput;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SourceOutput implements Serializable {

        private String status;

        private boolean gstAssociatedWithPan;

        List<GstDetails> gstDetails;

    }

}
