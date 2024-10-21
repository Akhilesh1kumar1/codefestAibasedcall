package com.sr.capital.service.impl;

import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.Screens;
import com.sr.capital.kyc.service.DocDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserProgressServiceImpl {

    final DocDetailsService docDetailsService;


    public String getUserProgress(String tenantId){

        List<KycDocDetails<?>> kycDocDetailsList = docDetailsService.fetchDocDetailsByTenantId(tenantId);
        String currentState = Screens.PENDING_DOCUMENT.name();
        Set<String> completedDoc =new HashSet<>();
        if(CollectionUtils.isNotEmpty(kycDocDetailsList)){

            kycDocDetailsList.forEach(kycDocDetails -> {
                completedDoc.add(kycDocDetails.getDocType().name());
            });

            if(!completedDoc.contains(DocType.PERSONAL_ADDRESS.name())){
                currentState = Screens.PERSONAL_INFO.name();
            }else if(!completedDoc.contains(DocType.BUSINESS_ADDRESS.name())){
                currentState = Screens.BUSINESS_DETAILS.name();
            }
        }

        return currentState;
    }
}
