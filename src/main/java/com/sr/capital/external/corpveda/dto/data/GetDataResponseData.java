package com.sr.capital.external.corpveda.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields
public class GetDataResponseData {

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("type")
    private String type;

    @JsonProperty("code")
    private int code;

    @JsonProperty("reference_id")
    private long referenceId;

    @JsonProperty("data")
    private Map<String,Object> data; // or a more specific type based on your data

    @JsonProperty("report_status")
    private String reportStatus;
}
