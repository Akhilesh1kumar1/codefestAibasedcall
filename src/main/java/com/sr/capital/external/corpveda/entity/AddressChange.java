package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AddressChange {

    @JsonProperty("old_value")
    private String oldValue;
    @JsonProperty("new_value")
    private String newValue;
    @JsonProperty("date_of_change")
    private Date dateOfChange;
}
