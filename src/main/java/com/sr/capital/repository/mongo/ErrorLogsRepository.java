package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.ErrorLogs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogsRepository extends MongoRepository<ErrorLogs,String> {
}
