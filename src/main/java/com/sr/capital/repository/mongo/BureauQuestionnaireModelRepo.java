package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import com.sr.capital.entity.mongo.crif.BureauQuestionnaireModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BureauQuestionnaireModelRepo extends MongoRepository<BureauQuestionnaireModel<?>, String> {

}