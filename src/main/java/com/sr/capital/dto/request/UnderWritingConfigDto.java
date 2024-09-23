package com.sr.capital.dto.request;

import com.sr.capital.offer.calculator.helpers.ParameterName;
import lombok.Data;

import java.util.List;

@Data
public class UnderWritingConfigDto {

    List<UnderWritingConfigDetails> underWritingConfigDetails;

    public static class UnderWritingConfigDetails{

        private ParameterName name;
        private double value;
        private double score;
        private String condition;
        private Double weightage;

    }
}
