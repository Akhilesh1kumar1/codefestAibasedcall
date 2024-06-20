package com.sr.capital.service;

import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;

import java.io.IOException;
import java.util.List;

public interface ExternalService {

    Boolean validateRequest(String vendorToken,String vendorCode,String loneVendorId) throws InvalidVendorTokenException, InvalidVendorCodeException;

    LoanOfferDetails createLoanOffer(LoanOfferDetails loanOfferDetails ,String vendorToken,String vendorCode) throws InvalidVendorTokenException, InvalidVendorCodeException;

    List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId,String vendorToken,String vendorCode,String loanVendorName) throws IOException, InvalidVendorTokenException, InvalidVendorCodeException;


}
