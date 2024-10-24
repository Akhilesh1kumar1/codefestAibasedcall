package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.DisbursementDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisbursmentMongoRepository extends MongoRepository<DisbursementDetails,String> {

    DisbursementDetails findBySrCompanyIdAndLoanId(Long srCompanyId, UUID loanId);
}
