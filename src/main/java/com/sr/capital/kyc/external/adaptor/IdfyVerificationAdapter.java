package com.sr.capital.kyc.external.adaptor;


import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.external.exception.IDfyAsyncTaskIdResponseException;
import com.sr.capital.kyc.external.exception.IDfyNameComparisonException;
import com.sr.capital.kyc.external.exception.IDfyVerificationException;
import com.sr.capital.kyc.external.request.verification.*;
import com.sr.capital.kyc.external.response.IdfyAsyncResponse;
import com.sr.capital.kyc.external.response.IdfyBaseResponse;
import com.sr.capital.kyc.external.response.verification.NameComparisonResponse;
import com.sr.capital.kyc.external.utill.IdfyUtil;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class IdfyVerificationAdapter {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private IdfyUtil idfyUtil;

    private final LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(IdfyVerificationAdapter.class);

    @Async
    @Retryable(maxAttemptsExpression = "${external.idfy.verify.retry.max.attempts:3}", backoff = @Backoff(delayExpression = "${external.idfy.verify.retry.backoff.millis:300}"))
    public <T> CompletableFuture<IdfyAsyncResponse> verifyDocuments(final T request)
        throws IDfyVerificationException, ServiceEndpointNotFoundException {
        Pair<String, TaskType> requestInfo = getRequestEndPointAndTaskType(request);

        try {
            return CompletableFuture.completedFuture(
                    webClientUtil.makeExternalCall(ServiceName.IDfy, kycAppProperties.getIdfyBaseUri(),
                            requestInfo.getFirst(), HttpMethod.POST, idfyUtil.getIDfyHeader(), null,
                            request, IdfyAsyncResponse.class)
            );
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new IDfyVerificationException(requestInfo.getSecond().name());
        }
    }

    @Async
    public CompletableFuture<IdfyBaseResponse[]> verificationResponseWithTaskId(final String requestId)
        throws IDfyAsyncTaskIdResponseException {
        try {
            return CompletableFuture.completedFuture(
                    webClientUtil.makeExternalCall(ServiceName.IDfy, kycAppProperties.getIdfyBaseUri(),
                            kycAppProperties.getIdfyAsyncTaskEndpoint(), HttpMethod.GET,
                            idfyUtil.getIDfyHeader(), idfyUtil.getTaskParameter(requestId),
                            null, IdfyBaseResponse[].class)
            );
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new IDfyAsyncTaskIdResponseException(requestId);
        }
    }

    public <T> NameComparisonResponse getNameComparisonScore(final T request)
        throws ServiceEndpointNotFoundException, IDfyNameComparisonException {
        Pair<String, TaskType> requestInfo = getRequestEndPointAndTaskType(request);

        try {
            return webClientUtil.makeExternalCall(ServiceName.IDfy,
                kycAppProperties.getIdfyBaseUri(), requestInfo.getFirst(), HttpMethod.POST,
                idfyUtil.getIDfyHeader(), null, request, NameComparisonResponse.class);
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new IDfyNameComparisonException(requestInfo.getSecond());
        }
    }

    private <T> Pair<String, TaskType> getRequestEndPointAndTaskType(final T request)
        throws ServiceEndpointNotFoundException {
        if (request instanceof GstVerificationRequest) {
            return Pair.of(kycAppProperties.getIdfyVerifyGSTDetailsEndpoint(), TaskType.GST);
        } else if (request instanceof PanCardVerificationRequest) {
            return Pair.of(kycAppProperties.getIdfyVerifyPancardDetailsEndpoint(), TaskType.PAN);
        } else if (request instanceof BankVerificationRequest) {
            return Pair.of(kycAppProperties.getIdfyVerifyBankDetailsEndpoint(), TaskType.BANK_DETAILS);
        } else if (request instanceof PanGstCrossVerificationRequest) {
            return Pair.of(kycAppProperties.getIdfyCrossVerifyPancardGSTEndpoint(), TaskType.PAN_GST);
        } else if (request instanceof PanAadhaarCrossVerificationRequest) {
            return Pair.of(kycAppProperties.getIdfyCrossVerifyPancardAadhaarEndpoint(), TaskType.PAN_AADHAAR);
        } else if (request instanceof NameComparisonRequest) {
            return Pair.of(kycAppProperties.getIdfyNameCompareEndpoint(), TaskType.PAN_BANK_DETAILS);
        } else if (request instanceof AadhaarVerificationRequest) {
            return Pair.of(kycAppProperties.getIdfyVerifyAadhaarDetailsEndpoint(), TaskType.AADHAAR);
        } else {
            throw new ServiceEndpointNotFoundException();
        }
    }

}
