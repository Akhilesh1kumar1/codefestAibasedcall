package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.kyc.GstCompleteDocDetails;
import com.sr.capital.repository.mongo.GstCompleteDocDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GstCompleteDetailsManager {

    final GstCompleteDocDetailsRepository gstCompleteDocDetailsRepository;

    public GstCompleteDocDetails saveGstDetails(GstCompleteDocDetails gstCompleteDocDetails){
         gstCompleteDocDetails= gstCompleteDocDetailsRepository.save(gstCompleteDocDetails);
         return gstCompleteDocDetails;
    }

    public GstCompleteDocDetails getGstDetailsByTenantIdAndGstInId(Long tenantId,String gstinId){
          return gstCompleteDocDetailsRepository.findBySrCompanyIdAndGstInNumber(tenantId,gstinId);
    }
}
