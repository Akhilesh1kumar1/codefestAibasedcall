package com.sr.capital.external.shiprocket.dto.kyc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.external.shiprocket.enums.KycStatus;
import com.sr.capital.external.shiprocket.enums.KycType;
import com.sr.capital.service.strategy.CustomSnakeCaseStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(CustomSnakeCaseStrategy.class)
public class KycSavedDetailPart {

	String companyType;
	KycType kycType;
	KycStatus status;
	String rejectionReason;
	String submittedOn;
	String selfieImage;
	String remarks;
	String modifiedBy;
	String verifiedAt;
	Boolean finalSave;
	KycDocument document1;
	KycDocument document2;
	String gstin;
	String businessName;

}