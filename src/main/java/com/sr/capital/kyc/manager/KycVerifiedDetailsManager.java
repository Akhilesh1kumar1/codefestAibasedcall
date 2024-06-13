package com.sr.capital.kyc.manager;


import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.exception.custom.IncompatibleDetailsException;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.repository.mongo.KycVerifiedDetailsRepo;
import com.sr.capital.util.MongoEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycVerifiedDetailsManager {

    @Autowired
    private KycVerifiedDetailsRepo kycVerifiedDetailsRepo;

    public KycVerifiedDetails<?> saveKycVerifiedDetails(KycVerifiedDetails<?> kycVerifiedDetails) throws IncompatibleDetailsException {
        if(!MongoEntityUtil.validateKycVerifiedDetails(kycVerifiedDetails)){
            throw new IncompatibleDetailsException();
        }
        return kycVerifiedDetailsRepo.save(kycVerifiedDetails);
    }

    public KycVerifiedDetails<?> findKycVerifiedDetailsByTenantIdAndTaskType(String tenantId, TaskType taskType) {
        return kycVerifiedDetailsRepo.findBySrCompanyIdAndTaskType(tenantId, taskType);
    }

    public List<KycVerifiedDetails<?>> findKycVerifiedDetailsByTenantId(String tenantId) {
        return kycVerifiedDetailsRepo.findBySrCompanyId(tenantId);
    }

}
