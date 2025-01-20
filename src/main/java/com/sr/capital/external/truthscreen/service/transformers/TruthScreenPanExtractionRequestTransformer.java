package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.extractions.TruthScreenPanDetailsExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenPanExtractionRequestTransformer implements TruthScreenExternalRequestTransformer {

    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        PanExtractionRequestData panExtractionRequestData = PanExtractionRequestData.builder().docNumber(request.getDocNumber()).transID(RequestData.getTenantId()+ HashUtil.generateRandomId()).docType(request.getDocType().getValue()).build();
        return (T) TruthScreenPanDetailsExtractionRequest.builder().details(panExtractionRequestData).build();
    }
}
