package com.sr.capital.offer.calculator.util;

import com.sr.capital.offer.calculator.model.Parameter;

import java.math.BigDecimal;

public class ScoringUtils {

    public static double calculateScoreForParameter(Parameter parameter, double value) {
        if ("greaterThan".equals(parameter.getCondition()) && value > parameter.getValue()) {
            return parameter.getScore();
        } else if ("lessThan".equals(parameter.getCondition()) && value < parameter.getValue()) {
            return parameter.getScore();
        } else if (parameter.getCondition().startsWith("range:")) {
            return calculateRange(parameter, value);
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

    private static double calculateRange(Parameter parameter, double value) {
        String[] parts = parameter.getCondition().replace("range:", "").split("-");
        boolean lowerLimit = parts[0].startsWith("<");
        double part1 = Double.parseDouble(parts[0].replaceAll("[^\\d.]", ""));
        double lowerBound = lowerLimit ? Double.MIN_VALUE : part1;

        double upperBound = parts.length > 1 ?
                Double.parseDouble(parts[1].replaceAll("[^\\d.]", "")) :
                (lowerLimit ? part1 : Double.MAX_VALUE);

        if (value >= lowerBound && value < upperBound) {
            return parameter.getScore();
        }

        return 0;
    }

}
