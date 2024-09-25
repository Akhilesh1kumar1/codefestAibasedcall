package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.WebhookHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookHistoryRepository extends MongoRepository<WebhookHistory,String> {
}
