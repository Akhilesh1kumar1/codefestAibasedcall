package com.sr.capital.repository.mongo;


import com.sr.capital.entity.mongo.SanctionDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanctionRepository extends MongoRepository<SanctionDetails,String> {

    SanctionDetails findBySrCompanyIdAndVendorSanctionCode(Long srCompanyId, String sanctionCode);
}
