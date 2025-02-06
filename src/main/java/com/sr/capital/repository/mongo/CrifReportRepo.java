package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrifReportRepo extends MongoRepository<CrifReport, String> {
    Optional<CrifReport> findByMobile(String mobile);

    List<CrifReport> findAllByConsentIdIn(List<String> consentIdList);

    @Query(value = "{}", fields = "{ 'mobile': 1, 'crifScore': 1 }")
    Page<CrifReport> findAllProjectedBy(Pageable pageable);
}