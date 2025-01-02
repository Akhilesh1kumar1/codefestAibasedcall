package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {


    private String description;
    @JsonProperty("hsn_code")
    private String hsnCode;
}
