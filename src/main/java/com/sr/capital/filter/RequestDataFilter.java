package com.sr.capital.filter;

import com.omunify.interceptor.utils.ThreadContextUtil;
import com.sr.capital.dto.RequestData;
import com.sr.capital.service.AuthenticatorService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import static com.omunify.filters.RequestContext.generateLoggingId;
import static com.sr.capital.helpers.constants.Constants.Headers.*;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDataFilter implements Filter {

    static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/*/health/**"),
            new AntPathRequestMatcher("/*/cache/**"),
            new AntPathRequestMatcher("/*/file-upload//cool-off-time"),
            new AntPathRequestMatcher("/*/external/**")
    );

    @Autowired
    AuthenticatorService authenticatorService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        authenticatorService.authenticateRequest(request);
        if (isPreflightRequest(request)) {
            chain.doFilter(req, res);
            return;
        }
        if (!validateRequestHeaders(request, response)) {
            return;
        }
        setRequestDataHeaders(request);
        chain.doFilter(req, res);
    }

    private void setRequestDataHeaders(HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_HEADER);
        setCorrelationId(correlationId);
      /*  String userId = request.getHeader(USER_HEADER);
        setUserId(userId);
        RequestData.setTenantId(request.getHeader(TENANT_HEADER));*/
    }

    private static void setUserId(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            RequestData.setUserId(Long.valueOf(userId));
        } else if (ThreadContextUtil.getUserContext() != null && ThreadContextUtil.getUserContext().getUserId() != null) {
            RequestData.setUserId(ThreadContextUtil.getUserContext().getUserId());
        }
    }

    private static void setCorrelationId(String correlationId) {
        if (StringUtils.isNotEmpty(correlationId)) {
            RequestData.setCorrelationId(correlationId);
        } else if (StringUtils.isEmpty(RequestData.getCorrelationId()) && ThreadContextUtil.getRequestContext() != null
                && StringUtils.isNotEmpty(ThreadContextUtil.getRequestContext().getRequestId())) {
            RequestData.setCorrelationId(ThreadContextUtil.getRequestContext().getRequestId());
        } else {
            RequestData.setCorrelationId(generateLoggingId());
        }
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return RequestMethod.OPTIONS.name().equals(request.getMethod());
    }

    private boolean validateRequestHeaders(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (StringUtils.isEmpty(request.getHeader(TENANT_HEADER)) && !PUBLIC_URLS.matches(request) && RequestData.getTenantId()==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tenant ID missing from headers");
            return false;
        }

        if ((ThreadContextUtil.getUserContext() == null || ThreadContextUtil.getUserContext().getUserId() == null)
                && StringUtils.isEmpty(request.getHeader(USER_HEADER)) && !PUBLIC_URLS.matches(request) && RequestData.getUserId()==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID missing from headers");
            return false;
        }

        return true;
    }
}
