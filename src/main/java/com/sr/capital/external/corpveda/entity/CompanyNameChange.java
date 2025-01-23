package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CompanyNameChange {


    @JsonProperty("has_company_name_change")
    private boolean hasCompanyNameChange;
    @JsonProperty("company_name_change")
    private List<String> companyNameChange;
}
