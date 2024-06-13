package com.sr.capital.kyc.dto.request.child;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateGstDocDetailsRequest {

    @JsonProperty("legal_name")
    private String legalName;

    @JsonProperty("trade_name")
    private String tradeName;

    @JsonProperty("gstin")
    private String gstin;
}
