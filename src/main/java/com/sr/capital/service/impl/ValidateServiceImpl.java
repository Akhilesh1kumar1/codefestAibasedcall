package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.ValidateService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ValidateServiceImpl implements ValidateService {

   final RequestValidationStrategy requestValidationStrategy;
   final LoanApplicationService loanApplicationService;
   final LoanAllocationServiceImpl loanAllocationService;
    @Override
    public Boolean validatePan(String value) throws Exception {
        return requestValidationStrategy.validateRequest(value, RequestType.PAN);
    }

    @Override
    public Boolean validateGst(String value) throws Exception {
        return requestValidationStrategy.validateRequest(value,RequestType.GST);
    }


    @Override
    public Boolean validateMobileNumberAndCreateLoanApplication(ValidateMobileNumberRequestDto validateMobileNumberRequestDto) throws Exception {
         Boolean validateMobileNumber =true;
        try {
             requestValidationStrategy.validateRequest(validateMobileNumberRequestDto,RequestType.VERIFY_MOBILE_FOR_LOAN);

         }catch (Exception ec){
             validateMobileNumber =false;
         }
        LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().build();
        loanAllocationService.getLoanVendor(loanMetaDataDto);
        LoanApplicationRequestDto requestDto =LoanApplicationRequestDto.builder().loanAmountRequested(BigDecimal.ZERO)
                .loanDuration(0).loanVendorId(loanMetaDataDto.getLoanVendorId()).loanVendorName(loanMetaDataDto.getLoanVendorName()).loanType("Flexi Loan")
                .loanStatus(LoanStatus.LEAD_DUPLICATE).createLoanAtVendor(false)
                .build();
        if(validateMobileNumber){
            requestDto.setLoanStatus(LoanStatus.LEAD_INITIATED);
        }
         loanApplicationService.submitLoanApplication(requestDto);
         return validateMobileNumber;
    }
}
