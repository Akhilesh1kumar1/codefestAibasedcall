package com.sr.capital.external.truthscreen.repository;


import com.sr.capital.external.truthscreen.entity.GSTinDetailsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GstinAggregateDataHistoryRepository extends MongoRepository<GSTinDetailsHistory,String> {
}
