package com.sr.capital.external.crif.service;

import com.sr.capital.external.crif.Constant.CrifDocumentType;
import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauQuestionnairePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CrifPartnerService {


    BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest);

    String getAccessCode();

    BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse);

    public BureauQuestionnaireResponse authenticationQuestionnaire(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest);

    BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest);

    Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel);

    Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest);

    Object verify(BureauInitiateResponse bureauInitiateResponse);

    List<CrifDocumentType> getDocType();
}

