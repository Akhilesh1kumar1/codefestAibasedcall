package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {

    private String city;
    private String country;
    private String full;

    @JsonProperty("line_1")
    private String line1;

    @JsonProperty("line_2")
    private String line2;

    private String state;

    @JsonProperty("street_name")
    private String streetName;

    private String zip;
}