package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.adapter.extractions.TruthScreenPanComprehensiveExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.PanComprehensiveExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenPanComprehensiveRequestTransformer implements TruthScreenExternalRequestTransformer {
    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        PanComprehensiveExtractionRequestData panComprehensiveExtractionRequestData = PanComprehensiveExtractionRequestData.builder().PanNumber(request.getDocNumber()).transID(request.getSrCompanyId()+ HashUtil.generateRandomId()).docType(request.getDocType().getValue()).build();
        request.setTransId(panComprehensiveExtractionRequestData.getTransID());
        return (T) TruthScreenPanComprehensiveExtractionRequest.builder().details(panComprehensiveExtractionRequestData).build();
    }
}
