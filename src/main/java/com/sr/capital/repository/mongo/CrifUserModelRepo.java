package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrifUserModelRepo extends MongoRepository<CrifUserModel, String> {

    Optional<CrifUserModel> findByMobile(String mobile);
}