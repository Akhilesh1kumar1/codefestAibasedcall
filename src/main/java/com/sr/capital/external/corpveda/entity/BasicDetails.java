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

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BasicDetails {

    @JsonProperty("cin")
    private String cin;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("roc_code")
    private String rocCode;
    @JsonProperty("company_status")
    private String companyStatus;
    @JsonProperty("registration_no")
    private String registrationNo;
    @JsonProperty("authorised_capital")
    private String authorisedCapital;
    @JsonProperty("paid_up_capital")
    private String paidUpCapital;
    @JsonProperty("company_pan")
    private String companyPan;
    @JsonProperty("date_of_incorporation")
    private Date dateOfIncorporation;
    @JsonProperty("date_of_last_agm")
    private Date dateOfLastAgm;
    @JsonProperty("date_of_balance_sheet")
    private Date dateOfBalanceSheet;
    @JsonProperty("company_category")
    private String companyCategory;
    @JsonProperty("company_sub_category")
    private String companySubCategory;
    @JsonProperty("class_of_company")
    private String classOfCompany;
    @JsonProperty("listing_status")
    private String listingStatus;
    @JsonProperty("sum_of_satisfied_charges")
    private String sumOfSatisfiedCharges;
    @JsonProperty("sum_of_active_charges")
    private String sumOfActiveCharges;
    @JsonProperty("lei_number")
    private String leiNumber;

}
