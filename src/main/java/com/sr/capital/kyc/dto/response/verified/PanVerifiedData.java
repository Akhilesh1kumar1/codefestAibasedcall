package com.sr.capital.kyc.dto.response.verified;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanVerifiedData {

    private String firstName;

    private String middleName;

    private String lastName;

    private String nameOnCard;

    private String idNumber;

    private String source;

    private Boolean aadhaarSeedingStatus;

    private String status;

    private boolean isActive;

    private String expiresAt;

}
