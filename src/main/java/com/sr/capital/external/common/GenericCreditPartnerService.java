package com.sr.capital.external.common;

import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.service.CreditPartnerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenericCreditPartnerService implements CreditPartnerService {
    @Override
    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
        return null;
    }

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode) {
        return null;
    }
}
