package com.sr.capital.external.crif.service;

import com.sr.capital.CommonConstant;
import com.sr.capital.entity.mongo.crif.CrifConsentDetails;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.repository.mongo.CrifConsentDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrifConsentDetailsService {

    private final CrifConsentDetailsRepo crifConsentDetailsRepo;
    private final RedissonClient redissonClient;




    public String getNextConsentSequencesFromDb() {
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

    public String getNextSequence() {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(Constants.RedisKeys.CRIF_CONSENT_ID_KEY);

        long currentSequence = atomicLong.incrementAndGet();

        if (currentSequence == 1) {
            String nextConsentSequencesFromDb = getNextConsentSequencesFromDb();

            atomicLong.set(Long.parseLong(nextConsentSequencesFromDb));
            currentSequence = Long.parseLong(nextConsentSequencesFromDb);
        }

        return String.valueOf(currentSequence);
    }

    public void save(CrifConsentDetails crifConsentDetails) {
        crifConsentDetailsRepo.save(crifConsentDetails);
    }

    public Page<CrifConsentDetails> findByExpiredAtBetweenAndStatus(String currentTime, String previousDayMidnight, Pageable pageable, String status) {
        return crifConsentDetailsRepo.findByExpiredAtBetweenAndStatus(currentTime, previousDayMidnight, status, pageable);
    }

    public CrifConsentDetails findByConsentId(String consentId) {
        return crifConsentDetailsRepo.findByConsentId(consentId);
    }

    public void saveAll(List<CrifConsentDetails> expiredDetails) {
        crifConsentDetailsRepo.saveAll(expiredDetails);
    }
}