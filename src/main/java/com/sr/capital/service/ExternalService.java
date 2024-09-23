package com.sr.capital.service;

import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.dto.request.KaleyraWebhookDto;
import com.sr.capital.kyc.dto.request.DocDetailsRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExternalService {

    Boolean validateRequest(String vendorToken,String vendorCode,String loneVendorId) throws InvalidVendorTokenException, InvalidVendorCodeException;

    LoanOfferDetails createLoanOffer(LoanOfferDetails loanOfferDetails ,String vendorToken,String vendorCode) throws InvalidVendorTokenException, InvalidVendorCodeException;

    List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId,String vendorToken,String vendorCode,String loanVendorName) throws IOException, InvalidVendorTokenException, InvalidVendorCodeException;

    String getCompanyWiseSalesDetails(String vendorToken,String vendorCode,String loanVendorName) throws Exception;

    public ResponseEntity<?> fetchDocDetailsByTenantId(final DocDetailsRequest docDetailsRequest,String vendorToken,String vendorCode,String loanVendorName) throws Exception;

    public ResponseEntity<?> saveWhatsAppCommunication(KaleyraWebhookDto content);

    public ResponseEntity<?> saveLoanStatus(String vendorToken,String vendorCode,String loanVendorName,Map<String ,Object> loanStatusWebhook) throws InvalidVendorTokenException, InvalidVendorCodeException, IOException;
}
