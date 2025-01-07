package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CrifReportRepo extends MongoRepository<CrifReport, String> {
    Optional<CrifReport> findByMobile(String mobile);

    List<CrifReport> findAllByConsentIdIn(List<String> consentIdList);
}