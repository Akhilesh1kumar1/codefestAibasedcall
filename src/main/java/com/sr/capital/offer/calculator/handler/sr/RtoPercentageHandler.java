package com.sr.capital.offer.calculator.handler.sr;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import lombok.Data;


public class RtoPercentageHandler implements ScopeHandler {

    private ScopeHandler next;

    @Override
    public void setNext(ScopeHandler next) {
        this.next =next;
    }

    @Override
    public double handle(OfferCalculatorRequestDto customInputData, Parameter parameter) {
        if ("rtoPercentage".equals(parameter.getName())) {
            double value = customInputData.getRtoPercentageInLastSixMonth();
            return calculateScoreForParameter(parameter, value);
        }
        if (next != null) {
            return next.handle(customInputData, parameter);
        }
        return 0;
    }

    private double calculateScoreForParameter(Parameter parameter, double value) {
        if (parameter.getCondition().equals("greaterThan") && value > parameter.getValue()) {
            return parameter.getScore();
        } else if (parameter.getCondition().equals("lessThan") && value < parameter.getValue()) {
            return parameter.getScore();
        }
        return 0;
    }
}
