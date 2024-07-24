package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstByPanDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.response.extraction.GstDetailsByPanResponse;
import com.sr.capital.kyc.external.response.extraction.data.GstDetailsByPanResponseData;
import com.sr.capital.kyc.external.response.extraction.data.PanCardExtractionResponseData;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GstByPanDocDetailsEntityConstructor implements EntityConstructor {


    @Override
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException {

        GstDetailsByPanResponse gstDetailsByPanResponse = (GstDetailsByPanResponse) request.getKarzaBaseResponse();
        GstByPanDocDetails gstByPanDocDetails =getPancardExtractionResponse(gstDetailsByPanResponse.getResult());
         if(request.getKycDocDetails()==null) {
             return (T) KycDocDetails.<GstByPanDocDetails>builder()
                     .srCompanyId(RequestData.getTenantId())
                     .docType(DocType.GST_BY_PAN)
                     .details(gstByPanDocDetails)
                     .build();
         }else{
             KycDocDetails<GstByPanDocDetails> gstDocDetails = (KycDocDetails<GstByPanDocDetails>) request.getKycDocDetails();
             gstDocDetails.setDetails(gstByPanDocDetails);
             return (T) request.getKycDocDetails();
         }
    }

    private GstByPanDocDetails getPancardExtractionResponse(List<GstDetailsByPanResponseData> extractionOutput) {
         GstByPanDocDetails gstByPanDocDetails =new GstByPanDocDetails();
         gstByPanDocDetails.setGstDetails(new ArrayList<>());

         extractionOutput.forEach(gstDetailsByPanResponseData -> {
             gstByPanDocDetails.getGstDetails().add(   GstByPanDocDetails.GstDetails.builder().pan(gstDetailsByPanResponseData.getPan())
                     .emailId(gstDetailsByPanResponseData.getEmailId()).gstinId(gstDetailsByPanResponseData.getGstinId()).gstinRefId(gstDetailsByPanResponseData.getGstinRefId())
                     .state(gstDetailsByPanResponseData.getState()).applicationStatus(gstDetailsByPanResponseData.getApplicationStatus())
                     .gstinStatus(gstDetailsByPanResponseData.getGstinStatus())
                     .authStatus(gstDetailsByPanResponseData.getAuthStatus())
                     .regType(gstDetailsByPanResponseData.getRegType())
                     .registrationName(gstDetailsByPanResponseData.getRegistrationName())
                     .tinNumber(gstDetailsByPanResponseData.getTinNumber()).mobNum(gstDetailsByPanResponseData.getMobNum())
                     .build());

         });

        return gstByPanDocDetails;
    }
}
