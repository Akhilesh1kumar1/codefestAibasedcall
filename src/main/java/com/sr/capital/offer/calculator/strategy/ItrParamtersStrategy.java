package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.gst.NetRevenueHandler;
import com.sr.capital.offer.calculator.handler.itr.ItrFilledOnTimeHandler;
import com.sr.capital.offer.calculator.handler.itr.ItrScoreHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class ItrParamtersStrategy implements OfferCalculationStrategy{

    private final ScopeHandler handlerChain;

    public ItrParamtersStrategy() {
        ScopeHandler itrFilledOnTimeHandler = new ItrFilledOnTimeHandler();
        ScopeHandler itrScoreHandler = new ItrScoreHandler();
        ScopeHandler netRevenueHandler = new NetRevenueHandler();

        itrFilledOnTimeHandler.setNext(itrScoreHandler);
        itrScoreHandler.setNext(netRevenueHandler);

        this.handlerChain = itrFilledOnTimeHandler;
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
