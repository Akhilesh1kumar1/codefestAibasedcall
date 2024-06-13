package com.sr.capital.kyc.dto.response.verified;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanAadhaarVerifiedData {

    private Boolean isLinked;

    private String message;

    private String status;

    private boolean isActive;

    private String expiresAt;

}
