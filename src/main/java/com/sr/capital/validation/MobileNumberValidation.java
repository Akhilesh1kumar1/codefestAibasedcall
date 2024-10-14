package com.sr.capital.validation;

import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateLoanDetailsResponse;
import com.sr.capital.helpers.enums.LoanVendorName;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.util.CoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobileNumberValidation implements RequestValidator {

    final CreditPartnerFactoryService creditPartnerFactoryService;

    @Override
    public <T, U> T validateRequest(U request) throws Exception {

        ValidateMobileNumberRequestDto validateMobileNumberRequestDto = (ValidateMobileNumberRequestDto) request;

        CoreUtil.validateMobile(validateMobileNumberRequestDto.getMobileNumber());

        String loanVendorName = validateMobileNumberRequestDto.getLoanVendorName()!=null?validateMobileNumberRequestDto.getLoanVendorName(): LoanVendorName.FLEXI.getLoanVendorName().toLowerCase();
        LoanMetaDataDto.ValidateLoanData validateLoanData = LoanMetaDataDto.ValidateLoanData.builder().mobileNumber(validateMobileNumberRequestDto.getMobileNumber()).build();

        LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().validateLoanData(validateLoanData).loanVendorName(loanVendorName).build();

        ValidateLoanDetailsResponse validateLoanDetailsResponse= (ValidateLoanDetailsResponse) creditPartnerFactoryService.getPartnerService(loanVendorName).validateLoanDetails(loanMetaDataDto);

        if(!validateLoanDetailsResponse.getSuccess()){
            throw new CustomException("Loan Already Exist for Given Mobile Number", HttpStatus.BAD_REQUEST);
        }
        return (T) Boolean.TRUE;
    }
}
