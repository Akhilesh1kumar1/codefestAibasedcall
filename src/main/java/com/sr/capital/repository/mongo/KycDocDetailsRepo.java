package com.sr.capital.repository.mongo;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.helpers.enums.DocType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycDocDetailsRepo extends MongoRepository<KycDocDetails<?>, String> {

    @Query("{ 'sr_company_id': ?0, 'doc_type': ?1}")
    KycDocDetails<?> findBySrCompanyIdAndDocType(String tenantId, DocType docType);

    List<KycDocDetails<?>> findBySrCompanyId(String tenantId);

    List<KycDocDetails<?>> findBySrCompanyIdIn(List<String> tenantId);

}