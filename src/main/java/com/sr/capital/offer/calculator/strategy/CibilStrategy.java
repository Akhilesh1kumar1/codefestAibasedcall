package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.gst.CommercialCibilScoreHandler;
import com.sr.capital.offer.calculator.handler.gst.PersonalCibilScoreHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class CibilStrategy implements OfferCalculationStrategy{
    private final ScopeHandler handlerChain;

    public CibilStrategy(){
        ScopeHandler personalCibilScoreHandler = new PersonalCibilScoreHandler();
        ScopeHandler commercialCibilScoreHandler = new CommercialCibilScoreHandler();

        personalCibilScoreHandler.setNext(commercialCibilScoreHandler);

        this.handlerChain = personalCibilScoreHandler;
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
