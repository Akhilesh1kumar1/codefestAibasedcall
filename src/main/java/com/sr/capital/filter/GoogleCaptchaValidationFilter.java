package com.sr.capital.filter;

import com.sr.capital.config.AppProperties;
import com.sr.capital.config.properties.BypassProperties;
import com.sr.capital.service.GoogleCaptchaService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GoogleCaptchaValidationFilter implements Filter {

    @Autowired
    private BypassProperties bypassProperties;

    @Autowired
    private GoogleCaptchaService googleCaptchaService;

    @Autowired
    AppProperties appProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest) || !(response instanceof HttpServletResponse httpResponse)) {
            chain.doFilter(request, response);
            return;
        }

        if (!Boolean.TRUE.equals(appProperties.getCaptchaVerificationEnabled())) {
            chain.doFilter(request, response);
            return;
        }

        log.info("[GOOGLE-CAPTCHA-VALIDATOR] Filter invoked!");

        String path = httpRequest.getRequestURI();
        String httpMethod = httpRequest.getMethod();

        Pair<Boolean, String> googleCaptchaFlagAndAction = bypassProperties.getGoogleCaptchaFlagAndAction(path, HttpMethod.valueOf(httpMethod));
        if (!Boolean.TRUE.equals(googleCaptchaFlagAndAction.getFirst())) {
            chain.doFilter(request, response);
            return;
        }



        String captchaAction = googleCaptchaFlagAndAction.getSecond();

        if (googleCaptchaService.verifyCaptcha(httpRequest, httpResponse, captchaAction)) {
            chain.doFilter(request, response);
        }
    }


    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
