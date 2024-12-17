package com.sr.capital.exception;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class ExceptionLoggingAspect {

//    @Autowired
//    private ExceptionLogRepository exceptionLogRepository;
//
//    // Define a pointcut for methods with the signature 'public Object callExternalApi(...)'
//    @Pointcut("execution(public * com.example.service.MyService.callExternalApi(..))")
//    public void callExternalApiPointcut() {
//    }
//
//    // This method is executed after an exception is thrown from 'callExternalApi'
//    @AfterThrowing(pointcut = "callExternalApiPointcut()", throwing = "ex")
//    public void logException(Exception ex) {
//        // Create a new ExceptionLog object to store the exception details
//        ExceptionLog exceptionLog = new ExceptionLog();
//        exceptionLog.setExceptionMessage(ex.getMessage());
//        exceptionLog.setMethodName("callExternalApi");
//        exceptionLog.setTimestamp(LocalDateTime.now());
//
//        // Save the exception log to the database
//        exceptionLogRepository.save(exceptionLog);
//    }
}
