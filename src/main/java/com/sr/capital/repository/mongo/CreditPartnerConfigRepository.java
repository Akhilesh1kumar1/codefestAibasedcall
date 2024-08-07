package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.CreditPartnerConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreditPartnerConfigRepository extends MongoRepository<CreditPartnerConfig, String> {

    CreditPartnerConfig findByPartnerId(Long creditPartnerId);

}
