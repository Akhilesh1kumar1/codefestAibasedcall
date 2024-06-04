package com.sr.capital.external.shiprocket.dto.kyc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KycSavedDetails {

    @JsonProperty("1")
	private KycSavedDetailPart n1;

    @JsonProperty("2")
	private KycSavedDetailPart n2;

}