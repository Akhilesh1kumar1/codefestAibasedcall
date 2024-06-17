package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.entity.LoanOffer;
import com.sr.capital.repository.LoanOfferRepository;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoanOfferServiceImpl implements LoanOfferService {

    final LoanOfferRepository loanOfferRepository;

    static final TypeReference<ArrayList<LoanOfferDetails>> TYPE_REF = new TypeReference<ArrayList<LoanOfferDetails>>() {
    };
    @Override
    public List<LoanOfferDetails> getLoanOfferDetails(Long srCompanyId, String status, Boolean enabled) {
        List<LoanOffer> loanOffers =null;
        if(status==null || status.equalsIgnoreCase("ALL")){
            loanOffers = loanOfferRepository.findBySrCompanyIdAndIsEnabled(srCompanyId,enabled);
        }else{
            loanOffers =loanOfferRepository.findBySrCompanyIdAndStatusAndIsEnabled(srCompanyId,status,enabled);
        }

        List<LoanOfferDetails> offerDetails =new ArrayList<>();

        loanOffers.forEach(loanOffer -> {offerDetails.add(LoanOfferDetails.mapLoanOffer(loanOffer));});
        return offerDetails;
    }
}
