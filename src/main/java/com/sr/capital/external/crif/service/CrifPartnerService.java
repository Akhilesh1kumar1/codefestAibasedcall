package com.sr.capital.external.crif.service;

import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.request.CrifConsentWithdrawalRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.*;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CrifPartnerService {


    BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CRIFApiException, CRIFApiLimitExceededException;

    String getAccessCode();

    BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse) throws CRIFApiException, CRIFApiLimitExceededException;

    BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest, boolean isRefreshRequest) throws CRIFApiException, CRIFApiLimitExceededException;

    Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) throws CustomException, CRIFApiException, CRIFApiLimitExceededException;

    Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CustomException, CRIFApiException, CRIFApiLimitExceededException;

    CrifResponse verify(BureauInitiateResponse bureauInitiateResponse) throws CustomException, CRIFApiException, CRIFApiLimitExceededException;

    Map<String, String> getDocType();

    void consentWithdrawalProcess(CrifConsentWithdrawalRequestModel crifConsentWithdrawalRequestModel) throws CRIFApiException;

    void purgeExpiredData();

    String saveAndGetConsentId();

    Page<CrifICRMReportRdo> getICRMReport(int page, int size);

    String getScoreForGivenMobile(String mobileNumber);
}

