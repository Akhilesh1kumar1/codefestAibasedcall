package com.sr.capital.external.klub.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.service.CreditPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KlubPartnerService  implements CreditPartnerService {

    final AppProperties appProperties;
    @Override
    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
        return null;
    }

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode) throws InvalidVendorCodeException, InvalidVendorTokenException {
        if(!appProperties.getKlubVendorCode().equalsIgnoreCase(vendorCode)){
            throw new InvalidVendorCodeException();
        }

        if(!appProperties.getKlubVendorToken().equalsIgnoreCase(vendorToken)){
            throw new InvalidVendorTokenException();
        }
        return true;
    }
}
