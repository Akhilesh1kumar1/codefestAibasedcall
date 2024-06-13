package com.sr.capital.kyc.dto.response.verified;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanGstVerifiedData {

    private String status;

    private Boolean gstAssociatedWithPan;

    private List<GstDetails> gstDetails;

    private boolean isActive;

    private String expiresAt;

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GstDetails {

        private String gstNumber;

        private String gstinStatus;

        private String state;

    }

}
