package com.sr.capital.kyc.service.constructor.entity.mongo;


import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.response.extraction.PanCardExtractionResponse;
import com.sr.capital.kyc.external.response.extraction.data.PanCardExtractionResponseData;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PanDocDetailsEntityConstructor implements EntityConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException {

        PanCardExtractionResponse response = (PanCardExtractionResponse) request.getIdfyBaseResponse();
        PanCardExtractionResponseData result =null;
        PanDocDetails panDocDetails =null;

        if(response!=null)
              result = response.getResult();
         else
            panDocDetails= MapperUtils.convertValue( request.getDocDetails(),PanDocDetails.class);

        List<String> images = new ArrayList<>();
        images.add(request.getFile1().getFileName());
        if(request.hasFile2()){
            images.add(request.getFile2().getFileName());
        }

        if (ObjectUtils.isNotEmpty(entity) || result==null) {
            KycDocDetails<?> kycDocDetails=null;
            if(result==null) {
                 kycDocDetails = KycDocDetails.builder().docType(request.getDocType()).srCompanyId(request.getSrCompanyId()).docType(request.getDocType()).kycType(request.getKycType()).details(panDocDetails).build();
            }else{
                 kycDocDetails = (KycDocDetails<PanDocDetails>) request.getKycDocDetails();
            }
            kycDocDetails.setImages(images);
            return (T) kycDocDetails;

        } else {
            return (T) KycDocDetails.<PanDocDetails>builder()
                    .srCompanyId(RequestData.getTenantId())
                    .docType(DocType.PAN)
                    .images(images)
                    .details(getPancardExtractionResponse(result.getExtractionOutput()))
                    .build();
        }
    }

    private PanDocDetails getPancardExtractionResponse(PanCardExtractionResponseData.ExtractionOutput extractionOutput) {
        return null;
    }


}
