package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.GstDocDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GstCompeteDocHistoryRepository extends MongoRepository<GstDocDetailsHistory,String> {
}
