package com.sr.capital.external.truthscreen.entity.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class TruthPanDocDetailsEntityConstructor implements TruthScreenEntityConstructor {

    @Autowired
    private AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        PanExtractionResponseData truthScreenPanExtractionResponse = MapperUtils.convertValue(request.getTruthScreenBaseResponse().getMsg(),PanExtractionResponseData.class);
        PanDetails panDetails = getPancardExtractionResponse(truthScreenPanExtractionResponse);

        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .truthScreenDocType(request.getDocType())
                .transId(request.getTransId())
                .details(panDetails)
                .build();
    }

    private PanDetails getPancardExtractionResponse(PanExtractionResponseData extractionOutput) {
        PanDetails panDetails = PanDetails.builder().docNumber(extractionOutput.getPanNumber())
                .name(extractionOutput.getName())
                .panHolderStatusType(extractionOutput.getPanHolderStatusType())
                .statusDescription(extractionOutput.getStatusDescription())
                .docNumber(extractionOutput.getPanNumber())
                .lastUpdate(extractionOutput.getLastUpdate())
                .status(extractionOutput.getStatus())
                .nameOnTheCard(extractionOutput.getNameOnTheCard())
                .sourceId(extractionOutput.getSourceId())
                .build();
        PanDetails.encryptInfo(panDetails,aes256);
        return panDetails;
    }

}
