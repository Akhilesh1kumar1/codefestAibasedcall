package com.sr.capital.service.entityimpl;


import com.sr.capital.entity.VerificationEntity;
import com.sr.capital.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public VerificationEntity findByVerificationToken(UUID verificationToken) {
        return verificationRepository.findById(verificationToken).orElse(null);
    }

}
