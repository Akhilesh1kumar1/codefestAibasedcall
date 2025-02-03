package com.sr.capital.external.corpveda.service.transformer.impl;

import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.data.CorpVedaGetOrderRequestExtractionData;
import com.sr.capital.external.corpveda.extractions.CorpVedaGetOrderExtractionRequest;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaRequestTransformer;
import org.springframework.stereotype.Service;

@Service
public class PartnerDetailsMetaDataRequestTransformer implements CorpVedaRequestTransformer {

    @Override
    public <T> T transformRequest(CorpVedaDocOrChestratorRequest request) throws Exception {
       CorpVedaGetOrderRequestExtractionData corpVedaGetOrderRequestExtractionData = CorpVedaGetOrderRequestExtractionData.builder().referenceId(request.getDocNumber()).build();
       return (T) CorpVedaGetOrderExtractionRequest.builder().details(corpVedaGetOrderRequestExtractionData).build();
    }

}
