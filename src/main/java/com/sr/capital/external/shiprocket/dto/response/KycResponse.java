package com.sr.capital.external.shiprocket.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.external.shiprocket.dto.kyc.KycSavedDetails;
import com.sr.capital.external.shiprocket.dto.kyc.KycVerificationDoc;
import com.sr.capital.helpers.strategy.CustomSnakeCaseStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(CustomSnakeCaseStrategy.class)
public class KycResponse {
    
	Boolean selfieEnable;
	Boolean manualImage;
	Boolean isManual;
	Boolean aadhaarEnable;
	Boolean gstEnable;
	Map<String, KycVerificationDoc> step1Verification;
	Map<String, KycSavedDetails> kycSavedDetails;

}
