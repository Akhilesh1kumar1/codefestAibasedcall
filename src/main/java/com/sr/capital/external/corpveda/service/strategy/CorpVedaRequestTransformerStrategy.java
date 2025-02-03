package com.sr.capital.external.corpveda.service.strategy;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.entity.PartnerDetailsMetaData;
import com.sr.capital.external.corpveda.enums.CorpVedaDocType;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaRequestTransformer;
import com.sr.capital.external.corpveda.service.transformer.impl.PartnerDetailsMetaDataRequestTransformer;
import com.sr.capital.external.corpveda.service.transformer.impl.PlaceOrderRequestTransformer;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchProviderException;

@Service
public class CorpVedaRequestTransformerStrategy {


    @Autowired
    private PlaceOrderRequestTransformer placeOrderTransformer;

    @Autowired
    private PartnerDetailsMetaDataRequestTransformer partnerDetailsMetaDataRequestTransformer;

    public <T extends CorpVedaBaseRequest<?>> T transformRequest(CorpVedaDocOrChestratorRequest request) throws Exception {
        CorpVedaRequestTransformer requestTransformer = null;
        CorpVedaDocType docType = request.getDocType();
        switch (docType) {
            case CIN_SEARCH_PLACE_ORDER:
                requestTransformer = placeOrderTransformer;
                break;
            case CIN_SEARCH_GET_DATA:
                requestTransformer = partnerDetailsMetaDataRequestTransformer;
                break;
            default:
                throw new RequestTransformerNotFoundException();
        }
        try {
            return requestTransformer.transformRequest(request);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }






}
