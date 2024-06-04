package com.sr.capital.external.shiprocket.dto.kyc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.service.strategy.CustomSnakeCaseStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(CustomSnakeCaseStrategy.class)
public class Step1Verification {

	KycVerificationDoc kyc1;
	KycVerificationDoc kyc2;

}