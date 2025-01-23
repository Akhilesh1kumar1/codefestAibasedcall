package com.sr.capital.external.corpveda.service.transformer.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.data.CorpVedaPlaceOrderRequestExtractionData;
import com.sr.capital.external.corpveda.extractions.CorpVedaCinSearchExtractionRequest;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderRequestTransformer implements CorpVedaRequestTransformer {

    @Autowired
    private AppProperties appProperties;

    @Override
    public <T> T transformRequest(CorpVedaDocOrChestratorRequest request) throws Exception {
        CorpVedaPlaceOrderRequestExtractionData corpVedaRequestExtractionData = CorpVedaPlaceOrderRequestExtractionData.builder().cin(request.getDocNumber()).clientId(appProperties.getCorpVedaClientId()).isDocumentsRequired(1).build();
        return (T) CorpVedaCinSearchExtractionRequest.builder().details(corpVedaRequestExtractionData).build();
    }
}
