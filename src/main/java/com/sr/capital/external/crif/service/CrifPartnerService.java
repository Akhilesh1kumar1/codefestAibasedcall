package com.sr.capital.external.crif.service;

import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauQuestionnairePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CrifPartnerService {


    BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CRIFApiException;

    String getAccessCode();

    BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse) throws CRIFApiException;

    BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest) throws CRIFApiException;

    Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) throws CustomException, CRIFApiException;

    Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CustomException, CRIFApiException;

    CrifResponse verify(BureauInitiateResponse bureauInitiateResponse) throws CustomException, CRIFApiException;

    Map<String, String> getDocType();
}

