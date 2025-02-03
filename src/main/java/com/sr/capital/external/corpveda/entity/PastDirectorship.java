package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PastDirectorship {

    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("date_of_appointment")
    private Date dateOfAppointment;
    @JsonProperty("cin")
    private String cin;
}
