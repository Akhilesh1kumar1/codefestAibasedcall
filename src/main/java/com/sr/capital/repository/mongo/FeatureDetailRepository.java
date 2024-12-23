package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.FeatureDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FeatureDetailRepository extends MongoRepository<FeatureDetails,String> {

    FeatureDetails findBySrCompanyId(Long srCompanyId);
}
