package com.sr.capital.service;

import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.dto.request.VerifyBankDetails;
import com.sr.capital.dto.response.TenantBankResponseDto;
import com.sr.capital.exception.custom.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TenantBankService {

    TenantBankResponseDto addBankDetails(BankDetailsRequestDto bankDetailsRequestDto, MultipartFile document) throws Exception;

    List<TenantBankResponseDto> getBankDetails(String userId);

    TenantBankResponseDto verifyBankDetails(VerifyBankDetails verifyBankDetails) throws Exception;
}
