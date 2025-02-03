package com.sr.capital.external.corpveda.repository;

import com.sr.capital.external.corpveda.entity.PartnerDetailsMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerDetailsMetaDataRepository extends MongoRepository<PartnerDetailsMetaData,String> {

    PartnerDetailsMetaData findBy(String tenantId);
}
