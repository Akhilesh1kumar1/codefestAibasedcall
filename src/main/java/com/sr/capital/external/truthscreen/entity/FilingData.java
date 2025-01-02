package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilingData {

    @JsonProperty("DateOfFiling")
    private String dateOfFiling;
    @JsonProperty("FinancialYear")
    private String financialYear;
    @JsonProperty("ModeOfFiling")
    private String modeOfFiling;
    @JsonProperty("ReturnType")
    private String returnType;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("TaxPeriod")
    private String taxPeriod;

}
