package com.sr.capital.external.truthscreen.entity.constructors;

import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.child.GstByPanDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenPanExtractionResponse;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.kyc.external.response.extraction.data.GstDetailsByPanResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TruthPanDocDetailsEntityConstructor implements TruthScreenEntityConstructor {


    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) {
        TruthScreenPanExtractionResponse response = (TruthScreenPanExtractionResponse) request.getTruthScreenBaseResponse();
        PanDetails panDetails = getPancardExtractionResponse(response.getMsg());
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .truthScreenDocType(TruthScreenDocType.PAN)
                .details(panDetails)
                .build();
    }

    private PanDetails getPancardExtractionResponse(PanExtractionResponseData extractionOutput) {
        PanDetails panDetails = new PanDetails();
        return panDetails.builder().docNumber(extractionOutput.getDocNumber())
                .name(extractionOutput.getName())
                .panHolderStatusType(extractionOutput.getPanHolderStatusType())
                .statusDescription(extractionOutput.getStatusDescription())
                .docNumber(extractionOutput.getDocNumber())
                .lastUpdate(extractionOutput.getLastUpdate())
                .status(extractionOutput.getStatus())
                .nameOnTheCard(extractionOutput.getNameOnTheCard())
                .sourceId(extractionOutput.getSourceId())
                .build();

    }
    }
