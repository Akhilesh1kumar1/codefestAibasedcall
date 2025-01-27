package com.sr.capital.filter;

import com.sr.capital.config.AppProperties;
import com.sr.capital.config.properties.BypassProperties;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.service.GoogleCaptchaService;
import com.sr.capital.service.RateLimiterService;
import com.sr.capital.util.FilterErrorUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.sr.capital.filter.RequestDataFilter.PUBLIC_URLS;

@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {

    private final RateLimiterService rateLimiterService;

    private final AppProperties appProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest req) || !(response instanceof HttpServletResponse res)
                || !Boolean.TRUE.equals(appProperties.getRateLimitEnabled())
        ) {
            chain.doFilter(request, response);
            return;
        }

        if(PUBLIC_URLS.matches(req)) {
            if (!rateLimiterService.isAllowed(req.getRemoteAddr())) {
                FilterErrorUtil.onError(res,
                        "Limit exceeded. Try again later.",
                        HttpStatus.TOO_MANY_REQUESTS
                );
                return;
            }
            else {
                chain.doFilter(request, response);
                return;
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
    }


    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
