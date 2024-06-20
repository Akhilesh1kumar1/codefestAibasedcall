package com.sr.capital.service.impl;

import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.service.CapitalDataReportService;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.ExternalService;
import com.sr.capital.service.LoanOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService {

    final LoanOfferService loanOfferService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    final CapitalDataReportService capitalDataReportService;

    @Override
    public Boolean validateRequest(String vendorToken, String vendorCode,String loanVendorName) throws InvalidVendorTokenException, InvalidVendorCodeException {

        return creditPartnerFactoryService.getPartnerService(loanVendorName).validateExternalRequest(vendorToken,vendorCode);
    }

    @Override
    public LoanOfferDetails createLoanOffer(LoanOfferDetails loanOfferDetails, String vendorToken, String vendorCode) throws InvalidVendorTokenException, InvalidVendorCodeException {
        validateRequest(vendorToken,vendorCode,loanOfferDetails.getLoanVendorName());
        return loanOfferService.saveLoanOffer(loanOfferDetails);
    }

    @Override
    public List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId,String vendorToken,String vendorCode,String loanVendorName) throws IOException, InvalidVendorTokenException, InvalidVendorCodeException {
        validateRequest(vendorToken,vendorCode,loanVendorName);
        return capitalDataReportService.getCompanySalesDetails(srCompanyId);
    }
}
