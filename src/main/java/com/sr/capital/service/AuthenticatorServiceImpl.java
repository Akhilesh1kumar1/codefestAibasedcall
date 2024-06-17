package com.sr.capital.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import com.sr.capital.external.shiprocket.client.ShiprocketClient;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.constants.Constants.Headers.TENANT_HEADER;
import static com.sr.capital.helpers.constants.Constants.Headers.TOKEN;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticatorServiceImpl implements AuthenticatorService {

    final ShiprocketClient shiprocketClient;
    @Override
    public ValidateTokenResponse authenticateRequest(HttpServletRequest req)  {


        ValidateTokenResponse response = null;
        try {
            response = shiprocketClient.validateToken(req.getHeader(TOKEN));
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }

        if(response!=null && response.getModel()!=null) {
            RequestData.setTenantId(String.valueOf(response.getModel().getCompanyId()));
            RequestData.setUserId(response.getModel().getTokenData().getSub());
        }
        return response;
    }
}
