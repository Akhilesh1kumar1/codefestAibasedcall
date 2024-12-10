package com.sr.capital.external.crif.controller;

import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.util.Base64Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/crif")
@RequiredArgsConstructor
public class CrifController {

    final CrifPartnerService crifPartnerService;

    @PostMapping(value = "/initiate")
    public ResponseEntity<?> crifStage1(@RequestBody BureauInitiatePayloadRequest bureauInitiatePayloadRequest
    ) throws Exception {

        BureauQuestionnaireResponse bureauQuestionnaireResponse = crifPartnerService.initiateBureau(bureauInitiatePayloadRequest);
        if (bureauQuestionnaireResponse != null && (bureauQuestionnaireResponse.getStatus().equals("S01") ||
                bureauQuestionnaireResponse.getStatus().equals("S10"))) {
            return getReport(bureauQuestionnaireResponse);
        }

        ResponseEntity<BureauQuestionnaireResponse> response = new ResponseEntity<>(bureauQuestionnaireResponse, HttpStatusCode.valueOf(200));


        return response;
    }

    @PostMapping(value = "/validate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crifStage2(@RequestBody BureauInitiateResponse bureauInitiateResponse) throws Exception {

        BureauQuestionnaireResponse questionnaire = crifPartnerService.getQuestionnaire(bureauInitiateResponse);

        if (questionnaire != null && (questionnaire.getStatus().equals("S01") || questionnaire.getStatus().equals("S10"))) {
            return getReport(questionnaire);
        }
        return new ResponseEntity<>(questionnaire, HttpStatusCode.valueOf(200));
    }

    @PostMapping(value = "/report", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crifStage3(@RequestBody BureauReportPayloadRequest bureauReportPayloadRequest
    ) throws Exception {

        BureauReportResponse report = crifPartnerService.getReport(bureauReportPayloadRequest);
        ResponseEntity<BureauReportResponse> response = new ResponseEntity<>(report, HttpStatusCode.valueOf(200));

        return response;

    }

    private ResponseEntity<?> getReport(BureauQuestionnaireResponse questionnaire) {
        BureauReportPayloadRequest bureauReportPayloadRequest = BureauReportPayloadRequest.builder()
                .reportId(questionnaire.getReportId())
                .orderId(questionnaire.getOrderId())
                .redirectURL(questionnaire.getRedirectURL())
                .build();
        BureauReportResponse report = crifPartnerService.getReport(bureauReportPayloadRequest);
        return new ResponseEntity<>(report, HttpStatusCode.valueOf(200));
    }

    @PostMapping(value = "/getAccessCode")
    public ResponseEntity<?> getAccessCode() throws Exception {

        String accessCode = crifPartnerService.getAccessCode();

        return new ResponseEntity<>(new String[]{accessCode, Base64Util.decode(accessCode)}, HttpStatusCode.valueOf(200));
    }
}