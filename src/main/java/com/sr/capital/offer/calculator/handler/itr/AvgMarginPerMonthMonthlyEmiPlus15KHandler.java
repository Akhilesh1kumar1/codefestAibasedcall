package com.sr.capital.offer.calculator.handler.itr;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

public class AvgMarginPerMonthMonthlyEmiPlus15KHandler implements ScopeHandler {
    private ScopeHandler next;

    @Override
    public void setNext(ScopeHandler next) {
        this.next = next;
    }

    @Override
    public double handle(OfferCalculatorRequestDto customInputData, Parameter parameter) {
        if (parameter.getName()==ParameterName.AVG_MARGIN_PER_MONTH_MONTHLY_EMI_PLUS_15K) {
            // Apply custom logic based on customInputData and parameter
            return parameter.getValue() * parameter.getWeightage();
        } else if (next != null) {
            return next.handle(customInputData, parameter);
        }
        return 0;
    }

}
