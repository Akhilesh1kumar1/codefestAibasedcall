package com.sr.capital.external.crif.service;

import com.sr.capital.entity.mongo.crif.CrifConsentDetails;
import com.sr.capital.repository.mongo.CrifConsentDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrifConsentDetailsService {

    private final CrifConsentDetailsRepo crifConsentDetailsRepo;


    public String getNextSequence() {
        CrifConsentDetails lastRecord = crifConsentDetailsRepo.findTopByOrderByConsentIdDesc();

        if (lastRecord != null && lastRecord.getConsentId() != null) {
            try {
                // Extract the numeric part of consentId and increment
                String lastConsentId = lastRecord.getConsentId();
                int currentSequence = Integer.parseInt(lastConsentId.replaceAll("\\D+", ""));
                return String.valueOf(currentSequence + 1);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid consentId format: " + lastRecord.getConsentId(), e);
            }
        }

        // Default starting value
        return String.valueOf(1);
    }

    public void save(CrifConsentDetails crifConsentDetails) {
        crifConsentDetailsRepo.save(crifConsentDetails);
    }

    public Page<CrifConsentDetails> findByExpiredAtBetween(String currentTime, String previousDayMidnight, Pageable pageable) {
        return crifConsentDetailsRepo.findByExpiredAtBetween(currentTime, previousDayMidnight, pageable);
    }

    public CrifConsentDetails findByConsentId(String consentId) {
        return crifConsentDetailsRepo.findByConsentId(consentId);
    }

    public void saveAll(List<CrifConsentDetails> expiredDetails) {
        crifConsentDetailsRepo.saveAll(expiredDetails);
    }
}