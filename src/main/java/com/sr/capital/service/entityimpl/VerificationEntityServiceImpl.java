package com.sr.capital.service.entityimpl;


import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.helpers.enums.VerificationStatus;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.repository.primary.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class VerificationEntityServiceImpl {

    @Autowired
    private VerificationRepository verificationRepository;

    public VerificationEntity saveVerificationEntity(VerificationEntity verificationEntity) {
        return verificationRepository.save(verificationEntity);
    }

    public VerificationEntity softDeleteVerificationEntity(VerificationEntity verificationEntity) {
        verificationEntity.setIsEnabled(false);
        return saveVerificationEntity(verificationEntity);
    }



    public VerificationEntity findByVerificationId(Long verificationToken) {
        return verificationRepository.findById(verificationToken).orElse(null);
    }


    public VerificationEntity findInProgressVerificationByTenantIdAndVerificationType(String tenantId, VerificationType verificationType) {
        List<VerificationEntity> verifications = verificationRepository.findByTenantIdVerificationTypeAndStatusList(tenantId, verificationType, List.of(VerificationStatus.PROCESSING, VerificationStatus.PENDING_FOR_MANUAL_APPROVAL));
        if(CollectionUtils.isEmpty(verifications)){
            return null;
        }
        return verifications.get(0);
    }




}
