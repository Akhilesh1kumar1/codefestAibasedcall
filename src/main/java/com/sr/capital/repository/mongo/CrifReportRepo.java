package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CrifReportRepo extends MongoRepository<CrifReport, String> {
    Optional<CrifReport> findByMobile(String mobile);
}