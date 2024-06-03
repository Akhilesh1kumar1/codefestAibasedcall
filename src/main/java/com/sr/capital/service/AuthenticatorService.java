package com.sr.capital.service;

import jakarta.servlet.ServletRequest;

public interface AuthenticatorService {

    boolean authenticateRequest(ServletRequest req);
}
