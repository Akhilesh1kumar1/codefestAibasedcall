package com.sr.capital.validation;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.entity.TenantBankDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.repository.TenantBankDetailsRepository;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.service.impl.CryptoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TenantBankRequestValidator implements RequestValidator {

     final AppProperties appProperties;
     final TenantBankDetailsRepository tenantBankDetailsRepository;
     final CryptoService cryptoService;

    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        BankDetailsRequestDto bankDetailsRequestDto = (BankDetailsRequestDto) request;

        // Data Decryption
        if(Boolean.TRUE.equals(appProperties.getRsaEncryptionEnabled())) {
            bankDetailsRequestDto.setAccountNumber(cryptoService.decryptDataUsingRsaPrivateKey(bankDetailsRequestDto.getAccountNumber()));
            bankDetailsRequestDto.setIfscCode(cryptoService.decryptDataUsingRsaPrivateKey(bankDetailsRequestDto.getIfscCode()));
            bankDetailsRequestDto.setStatementPassword(cryptoService.decryptDataUsingRsaPrivateKey(bankDetailsRequestDto.getStatementPassword()));
        }


        TenantBankDetails tenantBankDetails = tenantBankDetailsRepository.findByUserIdAndAccountNumber(bankDetailsRequestDto.getUserId(), bankDetailsRequestDto.getAccountNumber());
        if(tenantBankDetails!=null){
            throw new CustomException("Account already added", HttpStatus.BAD_REQUEST);
        }
        return (T) bankDetailsRequestDto;
    }
}
