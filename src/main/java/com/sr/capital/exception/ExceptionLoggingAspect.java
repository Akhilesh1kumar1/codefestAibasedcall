package com.sr.capital.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.ErrorLogs;
import com.sr.capital.external.dto.request.CommunicationRequestTemp;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.helpers.enums.CommunicationTemplateNames;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.service.entityimpl.ErrorLogsEntityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

    private final ErrorLogsEntityServiceImpl exceptionLogRepository;
    private final ObjectMapper objectMapper;
    private final CommunicationService communicationService;
    private final AppProperties appProperties;

    public ExceptionLoggingAspect(ErrorLogsEntityServiceImpl exceptionLogRepository, ObjectMapper objectMapper, CommunicationService communicationService, AppProperties appProperties) {
        this.exceptionLogRepository = exceptionLogRepository;
        this.objectMapper = objectMapper;
        this.communicationService = communicationService;
        this.appProperties = appProperties;
    }

    @Pointcut("execution(public * com.sr.capital.util.WebClientUtil.invokeWebClient*(..)) || " +
            "execution(public * com.sr.capital.util.WebClientUtil.makeExternalCall*(..))")
    public void allExternalCallsPointcut() {}

    @AfterThrowing(
            pointcut = "allExternalCallsPointcut() && " +
                    "args(serviceName, baseUri, endPoint, method, requestId, httpHeaders, parameters, body, ..)",
            throwing = "ex")
    public <T> void logException1(ServiceName serviceName, String baseUri, String endPoint, HttpMethod method, String requestId,
                                  HttpHeaders httpHeaders, Map<String, String> parameters, Object body, Exception ex) {

        ErrorLogs errorLogs = ErrorLogs.builder()
                .header(httpHeaders != null ? httpHeaders.toSingleValueMap().toString() : "")
                .endPoint(baseUri + endPoint)
                .serviceName(serviceName.getName())
                .requestBody(body)
                .requestParam(parameters != null ? parameters.toString() : "")
                .srCompanyId(RequestData.getTenantId())
                .errorMessage(ex.getMessage())
                .build();

        ErrorLogs errorLogs1 = exceptionLogRepository.saveErrorLogs(errorLogs);
        triggerEmail(serviceName.getName(), baseUri, endPoint, errorLogs1.getId(), ex);
    }

    private void triggerEmail(String serviceName, String baseUri, String endPoint, String id, Exception exception) {
        try {
            if (appProperties.getIsErrorMailTriggerEnabled() && !appProperties.getNetCoreSendEmailEndpoint().equals(endPoint)) {
                String subject = "Exception Alert: An Error Occurred";
                String templateName = CommunicationTemplateNames.EXCEPTION_ALERT_EMAIL.getTemplateName();
                String message = "Error while calling api " + baseUri + endPoint + " is " + exception.getMessage()
                        + " Error log id for reference  " + id;

                CommunicationRequestTemp.MetaData metaData = CommunicationRequestTemp.MetaData.builder()
                        .vendorName(serviceName)
                        .capitalUrl(appProperties.getCapitalWebUrl()).exception(message).build();

                String errorEmailRecipientName = appProperties.getErrorEmailRecipientName();
                if (errorEmailRecipientName != null) {
                    String[] split = errorEmailRecipientName.split(",");
                    for (String recipient : split) {
                        CommunicationRequestTemp.EmailCommunicationDTO emailCommunicationDTO = CommunicationRequestTemp.EmailCommunicationDTO.builder()
                                .recipientEmail(recipient).recipientName("").subject(subject).build();

                        CommunicationRequestTemp communicationRequestTemp = CommunicationRequestTemp.builder().contentMetaData(metaData).emailCommunicationDto(emailCommunicationDTO).templateName(templateName).build();

                        communicationService.sendCommunicationForLoan(communicationRequestTemp);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("error in communication  " + ex.getMessage());
        }
    }

    @Pointcut("execution(public * com.sr.capital.util.ProviderHelperUtil.makeApiCall*(..))")
    public void makeApiCallPointcut() {}

    @AfterThrowing(
            pointcut = "makeApiCallPointcut() && " +
                    "args(parameters, endPoint,body, ..)",
            throwing = "ex")
    public <T> void logExceptionForMakeApiCall(Map<String, Object> parameters, String endPoint, Object body, Exception ex) {
        ErrorLogs errorLogs = ErrorLogs.builder()
                .endPoint(endPoint)
                .serviceName("makeApiCall")
                .requestBody(body)
                .requestParam(parameters != null ? parameters.toString() : "")
                .srCompanyId(RequestData.getTenantId())
                .errorMessage(ex.getMessage())
                .build();

        ErrorLogs errorLogs1 = exceptionLogRepository.saveErrorLogs(errorLogs);
        triggerEmail("makeApiCall", endPoint, "", errorLogs1.getId(), ex);

    }
}
