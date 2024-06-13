package com.sr.capital.kyc.dto.response.verified;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GstVerifiedData {

    private String legalName;

    private String tradeName;

    private String gstin;

    private String gstinStatus;

    private String ConstitutionOfBusiness;

    private String centreJurisdiction;

    private String dateOfRegistration;

    private String doorNumber;

    private String street;

    private String stateName;

    private String pincode;

    private String dst;

    private String location;

    private String status;

    private boolean isActive;

    private String expiresAt;

}
