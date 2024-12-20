package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.entity.primary.PANDetails;
import com.sr.capital.external.truthscreen.adapter.extractions.TruthScreenPanDetailsExtractionRequest;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.kyc.external.request.PanCardExtractionRequest;
import com.sr.capital.util.HashUtil;
import com.sr.capital.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenPanExtractionRequestTransformer implements TruthScreenExternalRequestTransformer {

    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        PanExtractionRequestData panExtractionRequestData = PanExtractionRequestData.builder().docNumber(request.getDocNumber()).transId(RequestData.getTenantId()+ HashUtil.generateRandomId()).docType(request.getDocType().getValue()).build();
        return (T) TruthScreenPanDetailsExtractionRequest.builder().details(panExtractionRequestData).build();
    }
}
