package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyCharge {

    @JsonProperty("unique_id")
    private Integer uniqueId;

    @JsonProperty("address")
    private String address;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("charge_holder_name")
    private String chargeHolderName;

    @JsonProperty("charge_id")
    private String chargeId;

    @JsonProperty("date_of_creation")
    private Date dateOfCreation;

    @JsonProperty("date_of_modification")
    private Date dateOfModification;

    @JsonProperty("date_of_satisfaction")
    private Date dateOfSatisfaction;

    @JsonProperty("charge_status")
    private String chargeStatus;
}
