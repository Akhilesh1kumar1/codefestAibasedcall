package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerDetailsMetaData {

    @JsonProperty("sr_company_id")
    private String srCompanyId;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("code")
    private int code;

    @JsonProperty("type")
    private String type;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private PartnerDetailsSubMetaData data;

    @JsonProperty("reference_id")
    private long referenceId;

    @JsonProperty("report_status")
    private String reportStatus;

    @JsonProperty("download_link")
    private String downloadLink;
}
