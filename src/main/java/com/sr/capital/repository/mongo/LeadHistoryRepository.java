package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.LeadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeadHistoryRepository extends MongoRepository<LeadHistory,String> {

    List<LeadHistory> findByLeadId(String leadId);
}
