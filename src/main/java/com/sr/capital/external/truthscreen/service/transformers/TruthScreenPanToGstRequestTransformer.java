package com.sr.capital.external.truthscreen.service.transformers;


import com.sr.capital.external.truthscreen.adapter.extractions.TruthScreenPanToGstExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.PanToGstExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenPanToGstRequestTransformer implements TruthScreenExternalRequestTransformer {
    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        PanToGstExtractionRequestData panToGstExtractionRequestData = PanToGstExtractionRequestData.builder().trans_id(request.getSrCompanyId() + HashUtil.generateRandomId()).doc_type(String.valueOf(request.getDocType().getValue())).doc_number(request.getDocNumber()).build();
        request.setTransId(panToGstExtractionRequestData.getTrans_id());
        return (T) TruthScreenPanToGstExtractionRequest.builder().details(panToGstExtractionRequestData).build();
    }
}
