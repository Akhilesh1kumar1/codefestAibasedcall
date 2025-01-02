package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifConsentDetails;
import com.sr.capital.entity.mongo.crif.CrifReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrifConsentDetailsRepo extends MongoRepository<CrifConsentDetails, String> {

//    @Query("{ 'expired_at': { '$lt': ?0, '$gte': ?1 } }")
//    Page<CrifConsentDetails> findByExpiredAtBetween(String currentTime, String previousDayMidnight, Pageable pageable);

    CrifConsentDetails findTopByOrderByConsentIdDesc();
    CrifConsentDetails findByConsentId(String consentId);

    @Query("{ 'expired_at': { '$lt': ?0, '$gte': ?1 }, 'status': ?2 }")
    Page<CrifConsentDetails> findByExpiredAtBetweenAndStatus(String currentTime, String previousDayMidnight, String status, Pageable pageable);
}