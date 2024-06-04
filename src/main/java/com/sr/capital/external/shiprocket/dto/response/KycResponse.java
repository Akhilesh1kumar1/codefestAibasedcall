package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.external.shiprocket.dto.kyc.KycSavedDetails;
import com.sr.capital.external.shiprocket.dto.kyc.Step1Verification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KycResponse {
    
	Boolean selfieEnable;
	Boolean manualImage;
	Boolean isManual;
	Boolean aadhaarEnable;
	Boolean gstEnable;
	Step1Verification step1Verification;
	KycSavedDetails kycSavedDetails;
}
