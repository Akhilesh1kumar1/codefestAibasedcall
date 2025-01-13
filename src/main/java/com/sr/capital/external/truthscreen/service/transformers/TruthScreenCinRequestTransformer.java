package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.adapter.extractions.TruthScreenCinExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.CinExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenCinRequestTransformer implements TruthScreenExternalRequestTransformer {
    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        CinExtractionRequestData cinExtractionRequestData = CinExtractionRequestData.builder().transID(request.getSrCompanyId() + "_" + HashUtil.generateRandomId()).docType(request.getDocType().getValue()).docName(request.getDocNumber()).build();
        return (T) TruthScreenCinExtractionRequest.builder().details(cinExtractionRequestData).build();
    }
}
