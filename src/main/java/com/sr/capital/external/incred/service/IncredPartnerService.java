package com.sr.capital.external.incred.service;

import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.service.CreditPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncredPartnerService implements CreditPartnerService {
    @Override
    public AccessTokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
        return null;
    }
}
