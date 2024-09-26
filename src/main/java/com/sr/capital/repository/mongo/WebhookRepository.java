package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.WebhookDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookRepository extends MongoRepository<WebhookDetails,String> {
}
