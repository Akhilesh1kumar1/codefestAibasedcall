package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.sr.NumberOfShipmentsHandler;
import com.sr.capital.offer.calculator.handler.sr.RtoPercentageHandler;
import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class SrParametersStrategy implements OfferCalculationStrategy{

    private ScopeHandler handlerChain;

    public SrParametersStrategy() {
        // Create handlers
        NumberOfShipmentsHandler numberOfShipmentsHandler = new NumberOfShipmentsHandler();
        RtoPercentageHandler rtoPercentageHandler = new RtoPercentageHandler();

        // Set up the chain
        numberOfShipmentsHandler.setNext(rtoPercentageHandler);

        // Assign the chain to the strategy
        this.handlerChain = numberOfShipmentsHandler;
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
