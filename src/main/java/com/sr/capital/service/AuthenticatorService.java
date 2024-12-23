package com.sr.capital.service;

import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticatorService {

    ValidateTokenResponse authenticateRequest(HttpServletRequest req) throws CustomException;
}
