package com.sr.capital.external.truthscreen.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenNddDecryptResponseDto extends TruthScreenBaseResponse {

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
