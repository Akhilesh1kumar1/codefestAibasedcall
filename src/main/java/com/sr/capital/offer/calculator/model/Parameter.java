package com.sr.capital.offer.calculator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter {

    private ParameterName name;
    private double value;
    private double score;
    private String condition;
    private Double weightage;
}
