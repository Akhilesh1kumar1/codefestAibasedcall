package com.sr.capital.service;

import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;

public interface CreditPartnerService {

    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto);

    public Boolean validateExternalRequest(String vendorToken,String vendorCode) throws InvalidVendorCodeException, InvalidVendorTokenException;
}
