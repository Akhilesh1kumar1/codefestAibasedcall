package com.sr.capital.util;

import com.omunify.logger.enums.LogLevel;
import com.omunify.logger.service.CustomLogManager;
import com.omunify.logger.service.CustomLogger;
import com.sr.capital.helpers.constants.LoggingConstants;
import com.sr.capital.helpers.enums.ServiceName;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LoggerUtil {

    private final CustomLogger customLogger;

    public LoggerUtil(CustomLogger log) {
        this.customLogger = log;
    }

    public static LoggerUtil getLogger(final Class<?> clazz) {
        return new LoggerUtil(CustomLogManager.getLogger(clazz));
    }

    public void info(String message){
        log(LogLevel.INFO, message);
    }

    public void debug(String message){
        log(LogLevel.DEBUG, message);
    }

    public void error(String message){
        log(LogLevel.ERROR, message);
    }

    public void stackTrace(Exception e){
        StringWriter stack = new StringWriter();
        e.printStackTrace(new PrintWriter(stack));
        log(LogLevel.ERROR, stack.toString());
    }


    private <T> void log(LogLevel logLevel, String message) {
        customLogger.log(logLevel, message);
    }


    public void logExternalAPIResponse(ServiceName serviceName, String uri, HttpMethod method, String requestId,
                                       HttpStatus responseStatus, HttpHeaders responseHeaders, Object responseBody,
                                       Long latency, LogLevel level) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(LoggingConstants.SERVICE_NAME_KEY, serviceName);
            jsonObject.put(LoggingConstants.API_REQUEST_URI, uri);
            jsonObject.put(LoggingConstants.API_REQUEST_METHOD, method);
            jsonObject.put(LoggingConstants.API_REQUEST_ID, requestId);
            jsonObject.put(LoggingConstants.API_RESPONSE_STATUS_CODE, responseStatus);
            jsonObject.put(LoggingConstants.API_RESPONSE_TIME, latency);
            if(responseHeaders != null) {
                jsonObject.put(LoggingConstants.API_RESPONSE_HEADERS, responseHeaders);
            }
            if(responseBody != null) {
                jsonObject.put(LoggingConstants.API_RESPONSE_BODY, responseBody);
            }
            log(level, jsonObject.toString());
        } catch (JSONException e) {
            error("JSON exception while logging");
        } catch (Exception e){
            error("API Response couldn't be logged! Exception: " + e.getMessage());
        };
    }
}
