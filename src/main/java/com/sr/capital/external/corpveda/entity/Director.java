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
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Director {

    @JsonProperty("name")
    private String name;

    @JsonProperty("din")
    private String din;

    @JsonProperty("pan")
    private String pan;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("dob")
    private Date dob;

    @JsonProperty("father_name")
    private String fatherName;

    @JsonProperty("dsc_reg")
    private String dscReg;

    @JsonProperty("dsc_exp")
    private Date dscExp;

    @JsonProperty("date_of_appointment")
    private Date dateOfAppointment;

    @JsonProperty("appointed_as")
    private String appointedAs;

    @JsonProperty("address")
    private String address;

    @JsonProperty("current_designation")
    private String currentDesignation;
}
