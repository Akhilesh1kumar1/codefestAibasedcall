package com.sr.capital.external.truthscreen.repository;


import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TruthScreenDocDetailsRepository extends MongoRepository<TruthScreenDocDetails<?>,String> {

    //@Query("{ 'sr_company_id': ?0, 'truth_doc_type': ?1}")
    TruthScreenDocDetails<?> findBySrCompanyIdAndTruthScreenDocType(String srCompanyId, TruthScreenDocType truthScreenDocType);



}
