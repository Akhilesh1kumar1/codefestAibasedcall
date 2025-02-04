package com.sr.capital.repository.mongo.los;

import com.sr.capital.entity.mongo.los.FormProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FormProgressRepository extends MongoRepository<FormProgress, String> {
    Optional<FormProgress> findByReferenceId(String referenceId);
}
