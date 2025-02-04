package com.sr.capital.repository.mongo.los;


import com.sr.capital.entity.mongo.los.LosStatusEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LosStatusEntityRepository extends MongoRepository<LosStatusEntity, String> {

    LosStatusEntity findByLosUserEntityId(String userEntityId);
}