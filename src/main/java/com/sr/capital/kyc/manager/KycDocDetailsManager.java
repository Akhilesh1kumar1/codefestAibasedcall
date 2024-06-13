package com.sr.capital.kyc.manager;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycDocDetailsHistory;
import com.sr.capital.exception.custom.IncompatibleDetailsException;
import com.sr.capital.repository.mongo.KycDocDetailsHistoryRepo;
import com.sr.capital.repository.mongo.KycDocDetailsRepo;
import com.sr.capital.util.MongoEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sr.capital.helpers.enums.DocType;

import java.util.ArrayList;
import java.util.List;

@Service
public class KycDocDetailsManager {

    @Autowired
    private KycDocDetailsRepo kycDocDetailsRepo;

    @Autowired
    private KycDocDetailsHistoryRepo kycDocDetailsHistoryRepo;


    public KycDocDetails<?> saveKycDocDetails(KycDocDetails<?> kycDocDetails) throws IncompatibleDetailsException {
        if(kycDocDetails == null) {
            return null;
        }
        if(!MongoEntityUtil.validateKycDocDetails(kycDocDetails)){
            throw new IncompatibleDetailsException();
        }
        kycDocDetailsHistoryRepo.save(KycDocDetailsHistory.builder().docDetails(kycDocDetails).build());
        return kycDocDetailsRepo.save(kycDocDetails);
    }

    public List<KycDocDetails<?>> saveKycDocDetailsList(List<KycDocDetails<?>> kycDocDetailsList) throws IncompatibleDetailsException {
        List<KycDocDetailsHistory> kycDocDetailsHistories = new ArrayList<>();
        for(KycDocDetails<?> kycDocDetails : kycDocDetailsList){
            if(!MongoEntityUtil.validateKycDocDetails(kycDocDetails)){
                throw new IncompatibleDetailsException();
            }
            kycDocDetailsHistories.add(KycDocDetailsHistory.builder().docDetails(kycDocDetails).build());
        }
        kycDocDetailsHistoryRepo.saveAll(kycDocDetailsHistories);
        return kycDocDetailsRepo.saveAll(kycDocDetailsList);
    }

    public KycDocDetails<?> findKycDocDetailsByTenantIdAndDocType(String tenantId, DocType docType) {
        if(docType == null) {
            return null;
        }
        return kycDocDetailsRepo.findBySrCompanyIdAndDocType(tenantId, docType);
    }

    public List<KycDocDetails<?>> findKycDocDetailsByTenantId(String tenantId) {
        return kycDocDetailsRepo.findBySrCompanyId(tenantId);
    }

    public List<KycDocDetails<?>> findKycDocDetailsByListOfTenantId(List<String> tenantId) {
        return kycDocDetailsRepo.findBySrCompanyIdIn(tenantId);
    }

}
