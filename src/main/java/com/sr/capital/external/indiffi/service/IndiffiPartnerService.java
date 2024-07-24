package com.sr.capital.external.indiffi.service;

import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.service.CreditPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndiffiPartnerService implements CreditPartnerService {
    @Override
    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
        return null;
    }

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode) throws InvalidVendorCodeException, InvalidVendorTokenException {
        return null;
    }
}
