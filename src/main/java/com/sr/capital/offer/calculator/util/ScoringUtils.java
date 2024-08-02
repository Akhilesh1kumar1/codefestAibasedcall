package com.sr.capital.offer.calculator.util;

import com.sr.capital.offer.calculator.model.Parameter;

import java.math.BigDecimal;

public class ScoringUtils {

    public static double calculateScoreForParameter(Parameter parameter, double value) {
        if ("greaterThan".equals(parameter.getCondition()) && value > parameter.getValue()) {
            return parameter.getScore();
        } else if ("lessThan".equals(parameter.getCondition()) && value < parameter.getValue()) {
            return parameter.getScore();
        }
        return 0;
    }

    public static double calculateScoreForParameter(Parameter parameter, boolean value) {
        if ("equals".equals(parameter.getCondition()) ) {
            return parameter.getScore();
        }
        return 0;
    }

    public static double calculateScoreForParameter(Parameter parameter, BigDecimal value) {
        if ("greaterThan".equals(parameter.getCondition()) && value.compareTo(BigDecimal.valueOf(parameter.getValue())) > 0) {
            return parameter.getScore();
        } else if ("lessThan".equals(parameter.getCondition()) && value.compareTo(BigDecimal.valueOf(parameter.getValue())) < 0) {
            return parameter.getScore();
        }
        return 0;
    }

    public static double calculateScoreForParameter(Parameter parameter, String value) {
        if ("equals".equals(parameter.getCondition()) && value.equals(parameter.getValue())) {
            return parameter.getScore();
        }
        return 0;
    }


}
