package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.handler.ScopeHandler;
import com.sr.capital.offer.calculator.handler.inventory.InventoryTurnOverDataHandler;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public class InventoryDataStrategy implements OfferCalculationStrategy{
    private final ScopeHandler handlerChain;

   public InventoryDataStrategy(){
       ScopeHandler inventoryTurnOverDataHandler = new InventoryTurnOverDataHandler();

       this.handlerChain = inventoryTurnOverDataHandler;
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
