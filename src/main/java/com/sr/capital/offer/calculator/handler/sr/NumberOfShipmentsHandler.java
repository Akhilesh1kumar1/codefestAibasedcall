package com.sr.capital.offer.calculator.handler.sr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import lombok.Data;


public class NumberOfShipmentsHandler implements ScopeHandler {

    private ScopeHandler next;

    @Override
    public void setNext(ScopeHandler next) {
        this.next = next;
    }

    @Override
    public double handle(OfferCalculatorRequestDto customInputData, Parameter parameter) {
        if (parameter.getName()== ParameterName.NUMBER_OF_SHIPMENT) {
            double value = customInputData.getAvgNumberOfShipmentsMonthly();
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
