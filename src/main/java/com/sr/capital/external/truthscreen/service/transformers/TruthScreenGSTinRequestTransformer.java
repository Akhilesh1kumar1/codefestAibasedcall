package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.extractions.TruthScreenGstExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.GstinExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenGSTinRequestTransformer implements TruthScreenExternalRequestTransformer {
    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        GstinExtractionRequestData gstinExtractionRequestData = GstinExtractionRequestData.builder().docNumber(request.getDocNumber()).docType(request.getDocType().getValue()).transId(request.getSrCompanyId()+ HashUtil.generateRandomId()).build();
        request.setTransId(gstinExtractionRequestData.getTransId());
        return (T) TruthScreenGstExtractionRequest.builder().details(gstinExtractionRequestData).build();
    }
}
