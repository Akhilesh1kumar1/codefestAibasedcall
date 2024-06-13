package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.KycDocDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycDocDetailsHistoryRepo extends MongoRepository<KycDocDetailsHistory, String> {
}
