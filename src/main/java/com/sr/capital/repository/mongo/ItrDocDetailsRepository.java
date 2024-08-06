package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.ItrDocDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItrDocDetailsRepository  extends MongoRepository<ItrDocDetails,String> {

    ItrDocDetails findBySrCompanyId(Long srCompanyId);
}
