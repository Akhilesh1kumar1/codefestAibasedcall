package com.sr.capital.external.corpveda.service.strategy;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.service.transformer.impl.PlaceOrderRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorpVedaRequestTransformerStrategy {

    @Autowired
    private PlaceOrderRequestTransformer placeOrderTransformer;

    public <T extends CorpVedaBaseRequest<?>> T transformRequest(CorpVedaDocOrChestratorRequest request) throws Exception {
        return placeOrderTransformer.transformRequest(request);
    }






}
