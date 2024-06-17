package com.sr.capital.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticatorService {

    ValidateTokenResponse authenticateRequest(HttpServletRequest req) ;
}
