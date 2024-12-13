package com.sr.capital.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import com.sr.capital.external.shiprocket.client.ShiprocketClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.constants.Constants.Headers.TOKEN;

@Service
@RequiredArgsConstructor
public class AuthenticatorServiceImpl implements AuthenticatorService {

    final ShiprocketClient shiprocketClient;
    @Override
    public ValidateTokenResponse authenticateRequest(HttpServletRequest req) throws CustomException {


        ValidateTokenResponse response = null;
        try {
            response = shiprocketClient.validateToken(req.getHeader(TOKEN));
        } catch (UnirestException | CustomException e) {
            throw new CustomException(e.getMessage(),  HttpStatus.UNAUTHORIZED);
        }

        if(response!=null && response.getModel()!=null) {
            RequestData.setTenantId(String.valueOf(response.getModel().getCompanyId()));
            RequestData.setUserId(response.getModel().getTokenData().getSub());
        }
        return response;
    }
}
