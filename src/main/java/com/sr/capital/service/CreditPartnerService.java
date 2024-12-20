package com.sr.capital.service;

import com.sr.capital.dto.request.CreateLeadRequestDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.dto.response.CreateLeadResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.common.request.DocumentUploadRequestDto;
import com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface CreditPartnerService {

    Object getAccessToken(String partner) throws CustomException;

    Object createLead(String partner, CreateLeadRequestDto requestDto) throws CustomException;

    Boolean validateExternalRequest(String vendorToken,String vendorCode) throws InvalidVendorCodeException, InvalidVendorTokenException;

    long expiryDurationInMs(String futureDate, DateTimeFormatter formatter);


    Object getLoanDetails( LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object validateLoanDetails(LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object uploadDocument(LoanMetaDataDto loanMetaDataDto);


    Object getPendingDocuments(LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object fetchDisburmentDetails(LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object fetchSanctionDetails(LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object acceptOffer(LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object updateLead(String partner, UpdateLeadRequestDto requestDto) throws CustomException;

    Object getKFS(LoanMetaDataDto loanMetaDataDto) throws CustomException;

    Object rejectSanctionOffer(LoanMetaDataDto loanMetaDataDto) throws CustomException;



}
