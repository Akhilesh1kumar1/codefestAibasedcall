package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PartnerFreeDataSubDetails {


    @JsonProperty("basic_details")
    private BasicDetails basicDetails;

    @JsonProperty("contact_details")
    private ContactDetails contactDetails;

    @JsonProperty("company_charges")
    private List<CompanyCharge> companyCharges;

    @JsonProperty("current_director")
    private List<Director> currentDirectors;

    @JsonProperty("other_directorship")
    private List<OtherDirectorship> otherDirectorships;
}
