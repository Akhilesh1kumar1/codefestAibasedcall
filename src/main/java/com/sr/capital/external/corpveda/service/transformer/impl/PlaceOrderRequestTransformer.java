package com.sr.capital.external.corpveda.service.transformer.impl;

import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaRequestTransformer;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderRequestTransformer implements CorpVedaRequestTransformer {
    @Override
    public <T> T transformRequest(CorpVedaDocOrChestratorRequest request) throws Exception {
        return null;
    }
}
