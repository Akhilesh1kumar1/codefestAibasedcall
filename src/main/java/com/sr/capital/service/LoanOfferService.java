package com.sr.capital.service;

import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.entity.primary.LoanOffer;

import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDetails> getLoanOfferDetails(Long srCompanyId,String status ,Boolean enabled);

    LoanOfferDetails saveLoanOffer(LoanOfferDetails loanOfferDetails);

}
