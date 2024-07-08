package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadGenerationRepository extends MongoRepository<Lead,String> {
    List<Lead> findBySrCompanyId(Long srCompanyId);
}
