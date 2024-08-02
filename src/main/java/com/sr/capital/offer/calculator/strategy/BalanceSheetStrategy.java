package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.pl.*;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class BalanceSheetStrategy implements OfferCalculationStrategy{
    private final ScopeHandler handlerChain;

    public BalanceSheetStrategy(){
        ScopeHandler profitInLastOneYearHandler = new ProfitInLastOneYearHandler();
        ScopeHandler presenceOfLongTermBorrowingHandler = new PresenceOfLongTermBorrowingHandler();
        ScopeHandler spendOnMarketingExpenditureHandler = new SpendOnMarketingExpenditureHandler();
        ScopeHandler ebitdaHandler = new EbitdaHandler();
        ScopeHandler dscrHandler = new DscrHandler();
        ScopeHandler leverageHandler = new LeverageHandler();

        profitInLastOneYearHandler.setNext(presenceOfLongTermBorrowingHandler);
        presenceOfLongTermBorrowingHandler.setNext(spendOnMarketingExpenditureHandler);
        spendOnMarketingExpenditureHandler.setNext(ebitdaHandler);
        ebitdaHandler.setNext(dscrHandler);
        dscrHandler.setNext(leverageHandler);

        this.handlerChain = profitInLastOneYearHandler;
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
