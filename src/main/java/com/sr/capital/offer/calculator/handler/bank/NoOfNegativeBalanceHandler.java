package com.sr.capital.offer.calculator.handler.bank;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import com.sr.capital.offer.calculator.util.ScoringUtils;

public class NoOfNegativeBalanceHandler implements ScopeHandler {
    private ScopeHandler next;

    @Override
    public void setNext(ScopeHandler next) {
        this.next = next;
    }

    @Override
    public double handle(OfferCalculatorRequestDto customInputData, Parameter parameter) {
        if (parameter.getName() == ParameterName.NO_OF_NEGATIVE_BALANCE) {
            double value = customInputData.getNoOFNegativeBalance(); // Assuming customInputData has a method to get the number of negative balances
            return ScoringUtils.calculateScoreForParameter(parameter, value);
        } else if (next != null) {
            return next.handle(customInputData, parameter);
        }
        return 0;
    }
}
