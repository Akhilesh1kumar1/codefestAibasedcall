package com.sr.capital.service;

import com.sr.capital.dto.request.*;
import com.sr.capital.dto.response.*;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.exception.custom.CustomException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface LoanApplicationService {

    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplication) throws Exception;

    public List<LoanApplicationResponseDto> getLoanApplication(UUID loanApplicationId);

    public List<Object[]> getLoanApplicationStatusByCompanyId(Long srCompanyId);

   public LoanApplication getLoanApplicationById(UUID loanApplicationId);

   public PendingDocumentResponseDto fetchPendingDocuments(PendingDocumentRequestDto pendingDocumentRequestDto) throws CustomException, IOException;

   public LoanApplicationResponseDto createLoanAtVendor(CreateLoanAtVendorRequest createLoanAtVendorRequest) throws CustomException;

    public SyncDocumentResponseDto syncDocumentToVendor(SyncDocumentToVendor syncDocumentToVendor) throws CustomException;

    public LoanApplication updateLoanApplication(LoanApplication loanApplication);


    public EnachRedirectionUrlResponseDto getRedirectionurl(EnachRedirectUrlRequestDto enachRedirectUrlRequestDto);


}
