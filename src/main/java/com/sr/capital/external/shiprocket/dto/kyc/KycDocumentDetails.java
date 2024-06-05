package com.sr.capital.external.shiprocket.dto.kyc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.external.shiprocket.enums.KycDocumentType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KycDocumentDetails {

	KycDocumentType documentType;
	String documentValue;
	String imageUrl;
	String name;
	String number;

	public void setDocumentType(String docType) {
		KycDocumentType type = null;
		if (docType != "") {
			type = KycDocumentType.fromValue(docType);
		}
		this.documentType = type;
	}

}
