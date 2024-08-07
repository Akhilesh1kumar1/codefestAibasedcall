package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.UnderWritingConfigHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnderWritingConfigHistoryRepository extends MongoRepository<UnderWritingConfigHistory,String> {
}
