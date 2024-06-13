package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtractedGstResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("trade_name")
    private String tradeName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("gstin")
    private String gstin;

    @JsonProperty("constitution_of_business")
    private String constitutionOfBusiness;

    @JsonProperty("type_of_registration")
    private String typeOfRegistration;

    @JsonProperty("pan_number")
    private String panNumber;

    @JsonProperty("date_of_liability")
    private String dateOfLiability;

    @JsonProperty("valid_up_to")
    private String validUpTo;

    @JsonProperty("is_provisional")
    private boolean isProvisional;
}
