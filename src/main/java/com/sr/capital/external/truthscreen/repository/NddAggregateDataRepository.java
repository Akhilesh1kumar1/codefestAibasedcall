package com.sr.capital.external.truthscreen.repository;

import com.sr.capital.external.truthscreen.entity.NddDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NddAggregateDataRepository extends MongoRepository<NddDetails,String> {
}
