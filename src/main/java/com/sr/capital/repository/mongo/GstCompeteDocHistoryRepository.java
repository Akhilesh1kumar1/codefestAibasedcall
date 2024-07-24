package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.GstDocDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GstCompeteDocHistoryRepository extends MongoRepository<GstDocDetailsHistory,String> {
}
