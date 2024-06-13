package com.sr.capital.dto;

import com.sr.capital.helpers.enums.RequestType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestData {

    private RequestData() {
        throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
    }

    static ThreadLocal<String> tenantIdLocal = new ThreadLocal<>();
    static ThreadLocal<String> correlationIdLocal = new ThreadLocal<>();
    static ThreadLocal<Long> userIdLocal = new ThreadLocal<>();

    static ThreadLocal<RequestType> requestTypeLocal = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        tenantIdLocal.set(tenantId);
    }

    public static String getTenantId() {
        return tenantIdLocal.get();
    }

    public static void setCorrelationId(String correlationId) {
        correlationIdLocal.set(correlationId);
    }

    public static String getCorrelationId() {
        return correlationIdLocal.get();
    }

    public static void setUserId(Long userId) {
        userIdLocal.set(userId);
    }

    public static Long getUserId() {
        return userIdLocal.get();
    }

    public static RequestType getRequestType() {
        return requestTypeLocal.get();
    }

    public static void setRequestType(RequestType requestType) {
        requestTypeLocal.set( requestType);
    }
}
