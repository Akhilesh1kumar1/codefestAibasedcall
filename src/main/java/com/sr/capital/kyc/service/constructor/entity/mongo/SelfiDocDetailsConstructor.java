package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.entity.mongo.kyc.child.SelfiDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelfiDocDetailsConstructor implements EntityConstructor {
    @Override
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException {
        List<String> images = new ArrayList<>();
        images.add(request.getFile1().getFileName());
        if(request.hasFile2()){
            images.add(request.getFile2().getFileName());
        }
       SelfiDocDetails selfiDocDetails= MapperUtils.convertValue( request.getDocDetails(), SelfiDocDetails.class);

        KycDocDetails<?> kycDocDetails = KycDocDetails.builder().docType(request.getDocType()).srCompanyId(request.getSrCompanyId()).docType(request.getDocType()).kycType(request.getKycType()).details(selfiDocDetails).build();
        kycDocDetails.setImages(images);
        return (T) kycDocDetails;
    }
}
