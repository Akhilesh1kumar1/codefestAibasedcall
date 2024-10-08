package com.sr.capital.service;

import com.sr.capital.dto.request.CreateLeadRequestDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.dto.response.CreateLeadResponseDto;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.common.request.DocumentUploadRequestDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface CreditPartnerService {

    Object getAccessToken(String partner);

    Object createLead(String partner, CreateLeadRequestDto requestDto);

    Boolean validateExternalRequest(String vendorToken,String vendorCode) throws InvalidVendorCodeException, InvalidVendorTokenException;

    long expiryDurationInMs(String futureDate, DateTimeFormatter formatter);


    Object getLoanDetails( LoanMetaDataDto loanMetaDataDto);

    Object validateLoanDetails(LoanMetaDataDto loanMetaDataDto);

    Object uploadDocument(List<DocumentUploadRequestDto> documentUploadRequestDto);


}
