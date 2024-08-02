package com.sr.capital.offer.calculator.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import lombok.Data;

public interface ScopeHandler {

    void setNext(ScopeHandler next);

    double handle(OfferCalculatorRequestDto customInputData, Parameter parameter);

}
