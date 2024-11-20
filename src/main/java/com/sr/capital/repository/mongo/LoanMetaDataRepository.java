package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.LoanMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanMetaDataRepository extends MongoRepository<LoanMetaData,String> {

    LoanMetaData findByLoanId(UUID loanId);
}
