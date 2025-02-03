package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PastDirector {

    @JsonProperty("name")
    private String name;
    @JsonProperty("din")
    private String din;
    @JsonProperty("pan")
    private String pan;
    @JsonProperty("nationality")
    private String nationality;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("dsc_reg")
    private String dscReg;
    @JsonProperty("dsc_exp")
    private Date dscExp;
    @JsonProperty("date_of_appointment")
    private Date dateOfAppointment;
    @JsonProperty("date_of_resignation")
    private Date dateOfResignation;
    @JsonProperty("address")
    private String address;
    @JsonProperty("appointed_as")
    private String appointedAs;
    @JsonProperty("current_designation")
    private String currentDesignation;
    @JsonProperty("begin_date")
    private Date beginDate;
}
