package com.sr.capital.util;

import com.omunify.logger.enums.LogLevel;
import com.omunify.logger.service.CustomLogManager;
import com.omunify.logger.service.CustomLogger;
import lombok.AllArgsConstructor;
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
}
