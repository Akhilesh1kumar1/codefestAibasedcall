package com.sr.capital.offer.calculator.strategy;

import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;

import java.util.List;

public interface OfferCalculationStrategy {

    double calculateScore(OfferCalculatorRequestDto customInputData, List<Parameter> parameters);

}
