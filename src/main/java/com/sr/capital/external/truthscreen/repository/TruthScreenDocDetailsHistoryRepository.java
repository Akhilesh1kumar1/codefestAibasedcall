package com.sr.capital.external.truthscreen.repository;

import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruthScreenDocDetailsHistoryRepository extends MongoRepository<TruthScreenDocDetailsHistory,String> {

}
