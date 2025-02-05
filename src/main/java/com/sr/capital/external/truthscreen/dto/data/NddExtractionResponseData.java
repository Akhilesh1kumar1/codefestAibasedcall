package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.external.truthscreen.dto.response.NDDDatabaseResponse;
import com.sr.capital.external.truthscreen.entity.NddDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NddExtractionResponseData {

    @JsonProperty("CREDIT_DEFAULT")
    private NDDDatabaseResponse creditDefault;
    @JsonProperty("Regulatory")
    private NDDDatabaseResponse regulatory;
    @JsonProperty("SANCTION")
    private NDDDatabaseResponse sanction;
    @JsonProperty("CRIMINAL")
    private NDDDatabaseResponse criminal;
    @JsonProperty("DEFAULTING_DIRECTORS_AND_COMPANIES")
    private NDDDatabaseResponse defaultDirectorsAndCompanies;
    private String status_code;
    private String message;
    private String tsTransId;

}
