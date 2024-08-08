package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.util.LoggerUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sr.capital.helpers.constants.Constants.ServiceConstants.X_GlAu_SECRET_KEY;


@Component
public class ServicesHandler {

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(ServicesHandler.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private ObjectMapper objectMapper;


    public void validateSelfSecret(HttpServletRequest servletRequest) throws InvalidCredentialsException {
        String appSecret = servletRequest.getHeader(X_GlAu_SECRET_KEY);
        if(!appProperties.getAppSecret().equals(appSecret)) {
            throw new InvalidCredentialsException("Unauthorized access");
        }
    }


}
