package com.sr.capital.dto;

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
}
