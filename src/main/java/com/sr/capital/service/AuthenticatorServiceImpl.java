package com.sr.capital.service;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticatorServiceImpl implements AuthenticatorService {
    @Override
    public boolean authenticateRequest(ServletRequest req) {
        return true;
    }
}
