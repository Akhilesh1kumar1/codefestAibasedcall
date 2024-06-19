package com.sr.capital.service;

import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.entity.primary.LoanOffer;

import java.util.List;
import java.util.UUID;

public interface LoanOfferService {

    List<LoanOfferDetails> getLoanOfferDetails(Long srCompanyId,String status ,Boolean enabled);

    LoanOfferDetails saveLoanOffer(LoanOfferDetails loanOfferDetails);

    LoanOffer getLoanOfferById(UUID loanOfferId);

}
