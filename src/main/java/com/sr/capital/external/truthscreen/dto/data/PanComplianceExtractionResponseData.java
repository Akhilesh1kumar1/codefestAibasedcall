package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PanComplianceExtractionResponseData {

    private boolean compliant;
    @JsonProperty("entity_name")
    private String entityName;
    @JsonProperty("financial_year")
    private String financialYear;
    @JsonProperty("is_specified")
    private String isSpecified;
    @JsonProperty("pan_aadhaar_link_status")
    private String panAadhaarLinkStatus;
    @JsonProperty("pan_allotment_date_string")
    private String panAllotmentDateString;
    @JsonProperty("pan_number")
    private String panNumber;
    @JsonProperty("pan_status")
    private String panStatus;
    @JsonProperty("transId")
    private String transId;
    @JsonProperty("tsTransId")
    private String tsTransId;
}
