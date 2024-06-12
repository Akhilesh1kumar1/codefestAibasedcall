package com.sr.capital.external.shiprocket.dto.kyc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.external.shiprocket.enums.KycStatus;
import com.sr.capital.external.shiprocket.enums.KycType;
import com.sr.capital.helpers.strategy.CustomSnakeCaseStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(CustomSnakeCaseStrategy.class)
public class KycSavedDetails {

	KycType kycType;
	KycStatus status;
	String rejectionReason;
	String submittedOn;
	String companyType;
	String selfieImage;
	String remarks;
	String modifiedBy;
	String verifiedAt;
	Boolean finalSave;
	String gstin;
	String businessName;
	KycDocumentDetails document1;
	KycDocumentDetails document2;

	public void setKycType(int kycType) {
		this.kycType = KycType.fromValue(kycType);
	}

	public void setStatus(String status) {
		KycStatus kycStatus = null;
		try {
			if (status != "") {
				kycStatus = KycStatus.fromValue(Integer.parseInt(status));
			}
		} finally {
			this.status = kycStatus;
		}
	}

}
