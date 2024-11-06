package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.LoanMetaData;
import com.sr.capital.repository.mongo.LoanMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanMetaDataEntityServiceImpl {

    final LoanMetaDataRepository loanMetaDataRepository;


    public LoanMetaData getLoanMetaDataDetails(UUID loanId){

        return loanMetaDataRepository.findByLoanId(loanId);
    }

    public LoanMetaData saveLoanMetaData(LoanMetaData loanMetaData){

        return loanMetaDataRepository.save(loanMetaData);
    }


}
