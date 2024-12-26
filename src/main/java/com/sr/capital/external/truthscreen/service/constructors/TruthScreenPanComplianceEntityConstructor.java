package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.PanComplianceExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.PanComplianceDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenPanComplianceEntityConstructor implements TruthScreenEntityConstructor {

    @Autowired
    private AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        PanComplianceExtractionResponseData panComplianceExtractionResponseData = MapperUtils.convertValue(request.getTruthScreenBaseResponse().getMsg(), PanComplianceExtractionResponseData.class);
        PanComplianceDetails panComplianceDetails = getPanComplianceExtractionResponse(panComplianceExtractionResponseData);
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .truthScreenDocType(request.getDocType())
                .details(panComplianceDetails)
                .build();
    }

    private PanComplianceDetails getPanComplianceExtractionResponse(PanComplianceExtractionResponseData extractionResponseData){
        PanComplianceDetails panComplianceDetails = PanComplianceDetails.builder().panStatus(extractionResponseData.getPanStatus())
                .panAllotmentDateString(extractionResponseData.getPanAllotmentDateString())
                .panAadhaarLinkStatus(extractionResponseData.getPanAadhaarLinkStatus())
                .entityName(extractionResponseData.getEntityName())
                .compliant(extractionResponseData.isCompliant())
                .financialYear(extractionResponseData.getFinancialYear())
                .isSpecified(extractionResponseData.getIsSpecified())
                .panNumber(extractionResponseData.getPanNumber())
                .transId(extractionResponseData.getTransId())
                .tsTransId(extractionResponseData.getTsTransId())
                .build();
        PanComplianceDetails.encrypt(panComplianceDetails,aes256);
        return panComplianceDetails;
    }
}
