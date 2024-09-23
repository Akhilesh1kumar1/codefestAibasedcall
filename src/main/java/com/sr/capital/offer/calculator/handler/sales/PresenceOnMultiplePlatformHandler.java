package com.sr.capital.offer.calculator.handler.sales;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import com.sr.capital.offer.calculator.util.ScoringUtils;

public class PresenceOnMultiplePlatformHandler implements ScopeHandler {
    private ScopeHandler next;

    @Override
    public void setNext(ScopeHandler next) {
        this.next = next;
    }

    @Override
    public double handle(OfferCalculatorRequestDto customInputData, Parameter parameter) {
        if (parameter.getName() == ParameterName.PRESENCE_ON_MULTIPLE_PLATFORM) {
            boolean value = customInputData.getPresenceOnMultiplePlatform();
            return ScoringUtils.calculateScoreForParameter(parameter, value);
        } else if (next != null) {
            return next.handle(customInputData, parameter);
        }
        return 0;
    }
}
