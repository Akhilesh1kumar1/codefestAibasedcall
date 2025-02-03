package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {


    private String description;
    @JsonProperty("hsn_code")
    private String hsnCode;
    @JsonProperty("sac_code")
    private String sacCode;
}
