package com.sr.capital.external.corpveda.service;

import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.data.PlaceOrderServiceResponseData;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public interface CorpVedaHookService {

    public PlaceOrderServiceResponseData placeOrder(CorpVedaDocOrChestratorRequest request, HttpHeaders headers) throws IOException;
}
