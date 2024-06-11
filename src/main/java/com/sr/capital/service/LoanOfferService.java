package com.sr.capital.service;

import com.sr.capital.dto.response.LoanOfferDetails;

import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDetails> getLoanOfferDetails(Long srCompanyId,String status ,Boolean enabled);
}
