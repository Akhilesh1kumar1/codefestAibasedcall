package com.sr.capital.validation;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.dto.request.VerifyBankDetails;
import com.sr.capital.entity.TenantBankDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.repository.TenantBankDetailsRepository;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.service.impl.CryptoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerifyBankDetailsRequestValidator implements RequestValidator {

    final AppProperties appProperties;
    final TenantBankDetailsRepository tenantBankDetailsRepository;
    final CryptoService cryptoService;
    @Override
    public <T, U> T validateRequest(U request) throws Exception {

        VerifyBankDetails bankDetailsRequestDto = (VerifyBankDetails) request;

        if(Boolean.TRUE.equals(appProperties.getRsaEncryptionEnabled())) {
            bankDetailsRequestDto.setAccountNumber(cryptoService.decryptDataUsingRsaPrivateKey(bankDetailsRequestDto.getAccountNumber()));
            bankDetailsRequestDto.setIfscCode(cryptoService.decryptDataUsingRsaPrivateKey(bankDetailsRequestDto.getIfscCode()));
        }
        Optional<TenantBankDetails> optionalTenantBankDetails = tenantBankDetailsRepository.findById(bankDetailsRequestDto.getId());
         TenantBankDetails tenantBankDetails;
        if(optionalTenantBankDetails.isEmpty()){
            throw new CustomException("Account details not found", HttpStatus.BAD_REQUEST);
        }else{
            tenantBankDetails = optionalTenantBankDetails.get();
        }

        TenantBankDetails tenantBankDetailsByAccountNumber = tenantBankDetailsRepository.findByUserIdAndAccountNumber(bankDetailsRequestDto.getUserId(), bankDetailsRequestDto.getAccountNumber());
        if(tenantBankDetailsByAccountNumber==null){
            throw new CustomException("Account details not found by account number", HttpStatus.BAD_REQUEST);
        }

        if(tenantBankDetailsByAccountNumber.getAccountNumber()!=tenantBankDetails.getAccountNumber()){
            throw new CustomException("Account details not matched", HttpStatus.BAD_REQUEST);
        }
        return (T) tenantBankDetails;
    }
}
