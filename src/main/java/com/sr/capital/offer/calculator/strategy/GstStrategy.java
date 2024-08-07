package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.gst.ComplianceCheckHandler;
import com.sr.capital.offer.calculator.handler.gst.GstRiskCheckHandler;
import com.sr.capital.offer.calculator.handler.gst.LifeTimeOfCompliancesHandler;
import com.sr.capital.offer.calculator.handler.gst.RecurringBusinessHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class GstStrategy implements  OfferCalculationStrategy{

    private final ScopeHandler handlerChain;

    public GstStrategy(){
        ScopeHandler gstRiskCheckHandler = new GstRiskCheckHandler();
        ScopeHandler complianceCheckHandler = new ComplianceCheckHandler();
        ScopeHandler lifeTimeOfCompliancesHandler = new LifeTimeOfCompliancesHandler();
        ScopeHandler recurringBusinessHandler = new RecurringBusinessHandler();

        gstRiskCheckHandler.setNext(complianceCheckHandler);
        complianceCheckHandler.setNext(lifeTimeOfCompliancesHandler);
        lifeTimeOfCompliancesHandler.setNext(recurringBusinessHandler);

        this.handlerChain = gstRiskCheckHandler;
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
