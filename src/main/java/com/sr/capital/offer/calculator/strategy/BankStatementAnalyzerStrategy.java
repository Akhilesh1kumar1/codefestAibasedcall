package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.bank.AvgBalanceHandler;
import com.sr.capital.offer.calculator.handler.bank.CurrentDPDHandler;
import com.sr.capital.offer.calculator.handler.bank.NoOfNegativeBalanceHandler;
import com.sr.capital.offer.calculator.handler.bank.RedFlagHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class BankStatementAnalyzerStrategy implements OfferCalculationStrategy{
    private final ScopeHandler handlerChain;

    public BankStatementAnalyzerStrategy(){
        ScopeHandler isRedFlagHandler = new RedFlagHandler();
        ScopeHandler avgBalanceHandler = new AvgBalanceHandler();
        ScopeHandler noOfNegativeBalanceHandler = new NoOfNegativeBalanceHandler();
        ScopeHandler currentDPDHandler = new CurrentDPDHandler();

        isRedFlagHandler.setNext(avgBalanceHandler);
        avgBalanceHandler.setNext(noOfNegativeBalanceHandler);
        noOfNegativeBalanceHandler.setNext(currentDPDHandler);

        this.handlerChain = isRedFlagHandler;
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
