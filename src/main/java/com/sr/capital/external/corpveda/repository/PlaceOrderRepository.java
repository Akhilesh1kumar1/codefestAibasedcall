package com.sr.capital.external.corpveda.repository;

import com.sr.capital.external.corpveda.entity.PartnerFreeDataDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceOrderRepository extends MongoRepository<PartnerFreeDataDetails,String> {

    PartnerFreeDataDetails findByReferenceId(String referenceId);


}
