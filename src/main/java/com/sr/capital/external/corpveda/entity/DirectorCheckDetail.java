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
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DirectorCheckDetail {
    @JsonProperty("name")
    private String name;
    @JsonProperty("din")
    private String din;
    @JsonProperty("din_status")
    private String dinStatus;
    @JsonProperty("disqualification_period_from")
    private String disqualificationPeriodFrom;
    @JsonProperty("disqualification_period_to")
    private String disqualificationPeriodTo;
}
