package com.sr.capital.external.crif.service;

import com.sr.capital.CommonConstant;
import com.sr.capital.entity.mongo.crif.CrifConsentDetails;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.repository.mongo.CrifConsentDetailsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrifConsentDetailsService {

    private final CrifConsentDetailsRepo crifConsentDetailsRepo;
    private final RedissonClient redissonClient;




    public String getNextConsentSequencesFromDb() {
        CrifConsentDetails lastRecord = crifConsentDetailsRepo.findTopByOrderByConsentIdDesc();

        if (lastRecord != null && lastRecord.getConsentId() != null) {
            try {
                // Extract the numeric part of consentId and increment
                Long lastConsentId = lastRecord.getConsentId();
                return String.valueOf(lastConsentId + 1);
            } catch (NumberFormatException e) {
                log.error("Error while parsing consentId format: " + lastRecord.getConsentId(), e);
            }
        }

        // Default starting value
        return String.valueOf(1);
    }

    public Long getNextSequence() {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(Constants.RedisKeys.CRIF_CONSENT_ID_KEY);

        long currentSequence = atomicLong.incrementAndGet();

        if (currentSequence == 1) {
            String nextConsentSequencesFromDb = getNextConsentSequencesFromDb();

            atomicLong.set(Long.parseLong(nextConsentSequencesFromDb));
            currentSequence = Long.parseLong(nextConsentSequencesFromDb);
        }

        return currentSequence;
    }

    public void save(CrifConsentDetails crifConsentDetails) {
        crifConsentDetailsRepo.save(crifConsentDetails);
    }

    public Page<CrifConsentDetails> findByExpiredAtBetweenAndStatus(String currentTime, String previousDayMidnight, Pageable pageable, String status) {
        return crifConsentDetailsRepo.findByExpiredAtBetweenAndStatus(currentTime, previousDayMidnight, status, pageable);
    }

    public CrifConsentDetails findByConsentId(String consentId) {
        return crifConsentDetailsRepo.findByConsentId(Long.valueOf(consentId));
    }

    public void saveAll(List<CrifConsentDetails> expiredDetails) {
        crifConsentDetailsRepo.saveAll(expiredDetails);
    }
}