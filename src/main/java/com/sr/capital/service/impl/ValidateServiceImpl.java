package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.ValidateService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.HashUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.sr.capital.helpers.constants.Constants.RedisKeys.GLOBAL_KEY;

@Service
@AllArgsConstructor
public class ValidateServiceImpl implements ValidateService {

   final RequestValidationStrategy requestValidationStrategy;
   final LoanApplicationService loanApplicationService;
   final LoanAllocationServiceImpl loanAllocationService;
   final CrifPartnerService crifPartnerService;
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
         Boolean validMobileNumber =true;
        try {
             requestValidationStrategy.validateRequest(validateMobileNumberRequestDto,RequestType.VERIFY_MOBILE_FOR_LOAN);

         }catch (Exception ec){
             validMobileNumber =false;
         }
        LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().build();
        loanAllocationService.getLoanVendor(loanMetaDataDto);
        LoanApplicationRequestDto requestDto =LoanApplicationRequestDto.builder().loanAmountRequested(BigDecimal.ZERO)
                .loanDuration(0).loanVendorId(loanMetaDataDto.getLoanVendorId()).loanVendorName(loanMetaDataDto.getLoanVendorName()).loanType("Flexi Loan")
                .loanStatus(LoanStatus.LEAD_DUPLICATE).internalLoanId(GLOBAL_KEY+ HashUtil.generateRandomId()).createLoanAtVendor(false)
                .build();
        if(validMobileNumber){
            requestDto.setLoanStatus(LoanStatus.LEAD_INITIATED);
        }
        requestDto.setCrifScore(crifPartnerService.getScoreForGivenMobile(validateMobileNumberRequestDto.getMobileNumber()));
        loanApplicationService.submitLoanApplication(requestDto);

         return validMobileNumber;
    }
}
