package com.sr.capital.service;

import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;

public interface CreditPartnerService {

    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto);
}
