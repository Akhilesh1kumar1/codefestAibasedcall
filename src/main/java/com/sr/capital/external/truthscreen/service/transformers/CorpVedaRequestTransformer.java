package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.config.AppProperties;
import com.sr.capital.external.truthscreen.dto.data.CorpVedaRequestExtractionData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.extractions.CorpVedaCinSearchExtractionRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class CorpVedaRequestTransformer implements TruthScreenExternalRequestTransformer {

    @Autowired
    private AppProperties appProperties;

    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        CorpVedaRequestExtractionData corpVedaExtractionData = CorpVedaRequestExtractionData.builder().cin("").isDocumentsRequired(1).clientId(appProperties.getCorpVedaClientId()).build();
        return (T) CorpVedaCinSearchExtractionRequest.builder().details(corpVedaExtractionData).build();
    }
}
