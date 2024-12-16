package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrifUserModelRepo extends MongoRepository<CrifUserModel, String> {

    Optional<CrifUserModel> findByMobile(String mobile);
    @Query("{ 'created_by': ?0 }")
    CrifUserModel findByCreatedBy(String createdBy);

}