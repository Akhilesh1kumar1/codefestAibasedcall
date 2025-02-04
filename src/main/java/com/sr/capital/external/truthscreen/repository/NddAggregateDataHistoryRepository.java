package com.sr.capital.external.truthscreen.repository;

import com.sr.capital.external.truthscreen.entity.NddDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NddAggregateDataHistoryRepository extends MongoRepository<NddDetailsHistory,String> {



}
