package com.sr.capital.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.ErrorLogs;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.service.entityimpl.ErrorLogsEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class ExceptionLoggingAspect {

    private final ErrorLogsEntityServiceImpl exceptionLogRepository;
    private final ObjectMapper objectMapper;

    public ExceptionLoggingAspect(ErrorLogsEntityServiceImpl exceptionLogRepository, ObjectMapper objectMapper) {
        this.exceptionLogRepository = exceptionLogRepository;
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(public * com.sr.capital.util.WebClientUtil.invokeWebClient*(..)) || " +
            "execution(public * com.sr.capital.util.WebClientUtil.makeExternalCall*(..)) || " +
            "execution(public * com.sr.capital.util.ProviderHelperUtil.makeApiCall*(..))")
    public void allExternalCallsPointcut() {}

    @AfterThrowing(
            pointcut = "allExternalCallsPointcut() && " +
                    "args(serviceName, baseUri, endPoint, method, requestId, httpHeaders, parameters, body, ..)",
            throwing = "ex")
    public <T> void logException1(ServiceName serviceName, String baseUri, String endPoint, HttpMethod method, String requestId,
                                  HttpHeaders httpHeaders, Map<String, String> parameters, Object body, Exception ex) {
    System.out.println("Exception caught: " );

        ErrorLogs errorLogs = ErrorLogs.builder()
                .header(httpHeaders != null ? httpHeaders.toSingleValueMap().toString() : "")
                .endPoint(baseUri + endPoint)
                .serviceName(serviceName.getName())
                .requestBody(body)
                .requestParam(parameters != null ? parameters.toString() : "")
                .srCompanyId(RequestData.getTenantId())
                .errorMessage(ex.getMessage())
                .build();

        exceptionLogRepository.saveErrorLogs(errorLogs);
    }
}
