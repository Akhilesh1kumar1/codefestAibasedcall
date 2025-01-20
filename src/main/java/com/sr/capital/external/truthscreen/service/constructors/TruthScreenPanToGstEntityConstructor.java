package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.PanToGstExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.PanToGstDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenPanToGstEntityConstructor implements TruthScreenEntityConstructor {

    @Autowired
    private AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        PanToGstExtractionResponseData panToGstExtractionResponseData = MapperUtils.convertValue(request.getTruthScreenBaseResponse(), PanToGstExtractionResponseData.class);
        PanToGstDetails panToGstDetails = getPanToGstDetails(panToGstExtractionResponseData);
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .truthScreenDocType(request.getDocType())
                .transId(request.getTransId())
                .initialStatus(request.getTruthScreenBaseResponse().getStatus())
                .details(panToGstDetails)
                .build();
    }

    private PanToGstDetails getPanToGstDetails(PanToGstExtractionResponseData extractionData) throws IOException {
        PanToGstDetails panToGstDetails = PanToGstDetails.builder().msg(extractionData.getMsg())
                .tsTransId(extractionData.getTsTransId())
                .status(extractionData.getStatus())
                .build();
        PanToGstDetails.encryptInfo(panToGstDetails, aes256);
        return panToGstDetails;
    }
}
