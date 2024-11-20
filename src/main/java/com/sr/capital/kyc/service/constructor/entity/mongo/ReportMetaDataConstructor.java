package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.entity.mongo.kyc.child.ReportMetaData;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.MapperUtils;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportMetaDataConstructor implements EntityConstructor {
    @Override
    public <T> T constructEntity(DocOrchestratorRequest request, T entity)  {
        List<String> images = new ArrayList<>();
        images.add(request.getFile1().getFileName());
        if(request.hasFile2()){
            images.add(request.getFile2().getFileName());
        }

        ReportMetaData reportMetaData =ReportMetaData.builder().metaData(request.getDocDetails()).build();
       if(request.getKycDocDetails()==null) {
           return (T) KycDocDetails.<ReportMetaData>builder()
                   .srCompanyId(RequestData.getTenantId())
                   .docType(request.getDocType())
                   .images(images)
                   .details(reportMetaData)
                   .build();
       }else{
           KycDocDetails<ReportMetaData> kyc = (KycDocDetails<ReportMetaData>) request.getKycDocDetails();
           kyc.setImages(images);
           kyc.setDetails(reportMetaData);
           kyc.setLastModifiedBy(RequestData.getUserId()==null?"SYSTEM":""+RequestData.getUserId());
           return (T) kyc;
       }

    }
}
