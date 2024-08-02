package com.sr.capital.offer.calculator.handler.sales;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import com.sr.capital.offer.calculator.util.ScoringUtils;

import java.math.BigDecimal;

public class GrowthInSalesComparedToTwelveMonthsHandler implements ScopeHandler {
    private ScopeHandler next;

    @Override
    public void setNext(ScopeHandler next) {
        this.next = next;
    }

    @Override
    public double handle(OfferCalculatorRequestDto customInputData, Parameter parameter) {
        if (parameter.getName() == ParameterName.GROWTH_IN_SALES_COMPARED_TO_TWELVE_MONTHS) {
            BigDecimal value = customInputData.getGrowthInSalesComparedToTwelveMonths(); // Assuming customInputData has a method to get this
            return ScoringUtils.calculateScoreForParameter(parameter, value);
        } else if (next != null) {
            return next.handle(customInputData, parameter);
        }
        return 0;
    }
}
