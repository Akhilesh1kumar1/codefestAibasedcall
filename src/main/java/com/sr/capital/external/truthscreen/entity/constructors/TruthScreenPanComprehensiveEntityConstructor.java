package com.sr.capital.external.truthscreen.entity.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.PanComprehensiveExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.ExtractionData;
import com.sr.capital.external.truthscreen.entity.PanComprehensiveDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenPanComprehensiveEntityConstructor implements TruthScreenEntityConstructor {

    @Autowired
    private AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        PanComprehensiveExtractionResponseData panComprehensiveExtractionResponseData = MapperUtils.convertValue(request.getTruthScreenBaseResponse(), PanComprehensiveExtractionResponseData.class);
        PanComprehensiveDetails panComprehensiveDetails = getPanComprehensiveExtractionResponse(panComprehensiveExtractionResponseData,request);
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .truthScreenDocType(request.getDocType())
                .transId(request.getTransId())
                .initialStatus(request.getTruthScreenBaseResponse().getStatus())
//                .tsTransId(request.getTruthScreenBaseResponse().getTsTransID())
                .details(panComprehensiveDetails)
                .build();
    }

    public PanComprehensiveDetails getPanComprehensiveExtractionResponse(PanComprehensiveExtractionResponseData extractionResponseData,TruthScreenDocOrchestratorRequest request) throws IOException {
        PanComprehensiveDetails panComprehensiveDetails = PanComprehensiveDetails.builder().data(MapperUtils.convertValue(extractionResponseData.getData(), ExtractionData.class))
                .messageCode(extractionResponseData.getMessageCode())
                .status(extractionResponseData.getStatus())
                .statusCode(extractionResponseData.getStatusCode())
                .success(extractionResponseData.isSuccess())
                .tsTransID(request.getTruthScreenBaseResponse().getTsTransID())
                .build();
        PanComprehensiveDetails.encryptInfo(panComprehensiveDetails,aes256);
        return panComprehensiveDetails;
    }
}
