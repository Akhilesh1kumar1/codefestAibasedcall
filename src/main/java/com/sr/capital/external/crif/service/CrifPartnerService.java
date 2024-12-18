package com.sr.capital.external.crif.service;

import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauQuestionnairePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CrifPartnerService {


    BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest);

    String getAccessCode();

    BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse);

    BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest);

    Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel);

    Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest);

    CrifResponse verify(BureauInitiateResponse bureauInitiateResponse);

    Map<String, String> getDocType();
}

