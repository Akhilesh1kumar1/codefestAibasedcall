package com.sr.capital.kyc.external.adaptor;


import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.external.exception.KarzaAsyncTaskIdResponseException;
import com.sr.capital.kyc.external.exception.KarzaNameComparisonException;
import com.sr.capital.kyc.external.exception.KarzaVerificationException;
import com.sr.capital.kyc.external.request.verification.*;
import com.sr.capital.kyc.external.response.KarzaAsyncResponse;
import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.verification.NameComparisonResponse;
import com.sr.capital.kyc.external.utill.KarzaUtil;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.concurrent.CompletableFuture;

@Component
public class KarzaVerificationAdapter {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private KarzaUtil karzaUtil;

    private final LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(KarzaVerificationAdapter.class);

    @Async
    @Retryable(maxAttemptsExpression = "${external.karza.verify.retry.max.attempts:3}", backoff = @Backoff(delayExpression = "${external.idfy.verify.retry.backoff.millis:300}"))
    public <T> CompletableFuture<KarzaAsyncResponse> verifyDocuments(final T request)
        throws KarzaVerificationException, ServiceEndpointNotFoundException {
        Pair<String, TaskType> requestInfo = getRequestEndPointAndTaskType(request);

        try {
            return CompletableFuture.completedFuture(
                    webClientUtil.makeExternalCall(ServiceName.KARZA, kycAppProperties.getKarzaBaseUri(),
                            requestInfo.getFirst(), HttpMethod.POST, karzaUtil.getKarzaHeader(), null,
                            request, KarzaAsyncResponse.class)
            );
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new KarzaVerificationException(requestInfo.getSecond().name());
        }
    }

    @Async
    public CompletableFuture<KarzaBaseResponse[]> verificationResponseWithTaskId(final String requestId)
        throws KarzaAsyncTaskIdResponseException {
        try {
            return CompletableFuture.completedFuture(
                    webClientUtil.makeExternalCall(ServiceName.KARZA, kycAppProperties.getKarzaBaseUri(),
                            kycAppProperties.getKarzaAsyncTaskEndpoint(), HttpMethod.GET,
                            karzaUtil.getKarzaHeader(), karzaUtil.getTaskParameter(requestId),
                            null, KarzaBaseResponse[].class)
            );
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new KarzaAsyncTaskIdResponseException(requestId);
        }
    }

    public <T> NameComparisonResponse getNameComparisonScore(final T request)
        throws ServiceEndpointNotFoundException, KarzaNameComparisonException {
        Pair<String, TaskType> requestInfo = getRequestEndPointAndTaskType(request);

        try {
            return webClientUtil.makeExternalCall(ServiceName.KARZA,
                kycAppProperties.getKarzaBaseUri(), requestInfo.getFirst(), HttpMethod.POST,
                karzaUtil.getKarzaHeader(), null, request, NameComparisonResponse.class);
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new KarzaNameComparisonException(requestInfo.getSecond());
        }
    }

    private <T> Pair<String, TaskType> getRequestEndPointAndTaskType(final T request)
        throws ServiceEndpointNotFoundException {
        if (request instanceof GstVerificationRequest) {
            return Pair.of(kycAppProperties.getKarzaVerifyGSTDetailsEndpoint(), TaskType.GST);
        } else if (request instanceof PanCardVerificationRequest) {
            return Pair.of(kycAppProperties.getKarzaVerifyPancardDetailsEndpoint(), TaskType.PAN);
        } else if (request instanceof BankVerificationRequest) {
            return Pair.of(kycAppProperties.getKarzaVerifyBankDetailsEndpoint(), TaskType.BANK_DETAILS);
        } else if (request instanceof PanGstCrossVerificationRequest) {
            return Pair.of(kycAppProperties.getKarzaCrossVerifyPancardGSTEndpoint(), TaskType.PAN_GST);
        } else if (request instanceof PanAadhaarCrossVerificationRequest) {
            return Pair.of(kycAppProperties.getKarzaCrossVerifyPancardAadhaarEndpoint(), TaskType.PAN_AADHAAR);
        } else if (request instanceof NameComparisonRequest) {
            return Pair.of(kycAppProperties.getKarzaNameCompareEndpoint(), TaskType.PAN_BANK_DETAILS);
        } else if (request instanceof AadhaarVerificationRequest) {
            return Pair.of(kycAppProperties.getKarzaVerifyAadhaarDetailsEndpoint(), TaskType.AADHAAR);
        } else {
            throw new ServiceEndpointNotFoundException();
        }
    }

}
