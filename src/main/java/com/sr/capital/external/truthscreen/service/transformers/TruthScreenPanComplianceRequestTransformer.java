package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.adapter.extractions.TruthScreenPanComplianceExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.PanComplianceExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenPanComplianceRequestTransformer implements TruthScreenExternalRequestTransformer {
    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        PanComplianceExtractionRequestData panComplianceEXtractionRequestData = PanComplianceExtractionRequestData.builder().panNumber(request.getDocNumber()).transID(request.getSrCompanyId()+ HashUtil.generateRandomId()).docType(request.getDocType().getValue()).build();
        return (T) TruthScreenPanComplianceExtractionRequest.builder().details(panComplianceEXtractionRequestData).build();
    }
}
