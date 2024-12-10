package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BureauInitiateModelRepo extends MongoRepository<BureauInitiateModel<?>, String> {

}