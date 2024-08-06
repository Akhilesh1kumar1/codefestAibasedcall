package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.ItrDocDetails;
import com.sr.capital.entity.mongo.kyc.ItrDocDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItrDocHistoryRepository extends MongoRepository<ItrDocDetailsHistory,String> {
}
