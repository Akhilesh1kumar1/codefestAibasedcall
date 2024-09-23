package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.sales.GrowthInSalesComparedToTwelveMonthsHandler;
import com.sr.capital.offer.calculator.handler.sales.GrowthInSalesHandler;
import com.sr.capital.offer.calculator.handler.sales.PresenceOnMultiplePlatformHandler;
import com.sr.capital.offer.calculator.handler.sales.SteadyRevenueHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class SalesDataStrategy implements OfferCalculationStrategy{
    private final ScopeHandler handlerChain;

    public SalesDataStrategy() {
        ScopeHandler presenceOnMultiplePlatformHandler = new PresenceOnMultiplePlatformHandler();
        ScopeHandler growthInSalesHandler = new GrowthInSalesHandler();
        ScopeHandler steadyRevenueHandler = new SteadyRevenueHandler();
        ScopeHandler growthInSalesComparedToTwelveMonthsHandler = new GrowthInSalesComparedToTwelveMonthsHandler();

        presenceOnMultiplePlatformHandler.setNext(growthInSalesHandler);
        growthInSalesHandler.setNext(steadyRevenueHandler);
        steadyRevenueHandler.setNext(growthInSalesComparedToTwelveMonthsHandler);

        this.handlerChain = presenceOnMultiplePlatformHandler;
    }

    @Override
    public double calculateScore(OfferCalculatorRequestDto customInputData, List<Parameter> parameters) {

        double totalScore = 0;
        for (Parameter parameter : parameters) {
            totalScore += handlerChain.handle(customInputData, parameter);
        }
        return totalScore;
    }
}
