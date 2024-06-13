package com.sr.capital.validation;

import com.sr.capital.dto.request.EnachLinkingRequestDto;
import com.sr.capital.entity.TenantBankDetails;
import com.sr.capital.repository.TenantBankDetailsRepository;
import com.sr.capital.service.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnachLinkingValidator implements RequestValidator {

    final TenantBankDetailsRepository tenantBankDetailsRepository;



    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        EnachLinkingRequestDto enachLinkingRequestDto = (EnachLinkingRequestDto) request;

        TenantBankDetails tenantBankDetails = tenantBankDetailsRepository.findByAccountNumberAndSrCompanyId( enachLinkingRequestDto.getAccountNumber(),enachLinkingRequestDto.getSrCompanyId());

        //TODO throw bank details not found exception
        if(tenantBankDetails==null){
        }

        if(enachLinkingRequestDto.getCapitalBankId()==null){
            enachLinkingRequestDto.setCapitalBankId(tenantBankDetails.getId());
        }

        return (T)enachLinkingRequestDto;
    }
}
