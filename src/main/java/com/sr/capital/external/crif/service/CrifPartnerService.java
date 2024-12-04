package com.sr.capital.external.crif.service;

import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauQuestionnairePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import org.springframework.stereotype.Service;

@Service
public interface CrifPartnerService {


    BureauQuestionnaireResponse initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest);

    BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse);

    public BureauQuestionnaireResponse authenticationQuestionnaire(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest);

    BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest);
}

