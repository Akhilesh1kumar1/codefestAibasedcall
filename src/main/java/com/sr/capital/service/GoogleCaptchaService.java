package com.sr.capital.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.response.GoogleCaptchaSiteVerificationResponse;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.CoreUtil;
import com.sr.capital.util.FilterErrorUtil;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.WebClientUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GoogleCaptchaService {

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AppProperties appProperties;
    public boolean verifyCaptcha(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String captchaAction) {
        String googleCaptchaResponse = httpRequest.getHeader(Constants.Headers.X_GOOGLE_CAPTCHA_RESPONSE);
        if (googleCaptchaResponse == null || googleCaptchaResponse.isEmpty()) {
            FilterErrorUtil.onError(httpResponse,
                    "Missing G-Recaptcha-Response!",
                    HttpStatus.NETWORK_AUTHENTICATION_REQUIRED
            );
            return false;
        }

        log.info("Validating Google ReCaptcha!");

        Map<String, String> params = new HashMap<>();
        params.put("secret", appProperties.getCaptchaSiteVerificationSecretKey());
        params.put("response", googleCaptchaResponse);
        String uri = CoreUtil.constructUri(appProperties.getGoogleBaseUri(), appProperties.getCaptchaSiteVerificationEndpoint(), params);

        Object responseBody = webClientUtil.makeExternalCallBlocking(ServiceName.CAPTCHA, appProperties.getGoogleBaseUri(), appProperties.getCaptchaSiteVerificationEndpoint(),
                HttpMethod.POST, null, new HttpHeaders(), null, params, Object.class);

        if (responseBody != null) {
            try {
                GoogleCaptchaSiteVerificationResponse verificationResponse =
                        MapperUtils.convertValue(responseBody, GoogleCaptchaSiteVerificationResponse.class);

                // Validate the response imperatively
                return validateCaptchaResponse(httpResponse, verificationResponse, captchaAction);
            } catch (Exception e) {
                log.error("Error parsing Google Captcha response", e);
                FilterErrorUtil.onError(httpResponse,
                        "Invalid G-Recaptcha-Response!",
                        HttpStatus.TOO_MANY_REQUESTS);
                return false;
            }
        } else {
            log.error("Empty response from Google Captcha verification");
            FilterErrorUtil.onError(httpResponse,
                    "Invalid G-Recaptcha-Response!",
                    HttpStatus.TOO_MANY_REQUESTS);
            return false;
        }
    }

    private boolean validateCaptchaResponse(HttpServletResponse httpResponse, GoogleCaptchaSiteVerificationResponse verificationResponse, String captchaAction) {
        if (!verificationResponse.getSuccess()) {
            FilterErrorUtil.onError(httpResponse,
                    "Captcha validation failed!",
                    HttpStatus.FORBIDDEN
            );
            return false;
        }

        if (!captchaAction.equals(verificationResponse.getAction())) {
            FilterErrorUtil.onError(httpResponse,
                    "Invalid action in captcha response!",
                    HttpStatus.FORBIDDEN
            );
            return false;
        }

        if (verificationResponse.getScore() < 0.5f) {
            FilterErrorUtil.onError(httpResponse,
                    "Low captcha score!",
                    HttpStatus.FORBIDDEN
            );
            return false;
        }

        log.info("Captcha validation successful for action: {}", captchaAction);
        return true;
    }
}
