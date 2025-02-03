package com.sr.capital.external.truthscreen.repository;

import com.sr.capital.external.truthscreen.entity.FilingData;
import com.sr.capital.external.truthscreen.entity.GSTinDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GstinAggregateDataRepository extends MongoRepository<GSTinDetails,String> {

}
