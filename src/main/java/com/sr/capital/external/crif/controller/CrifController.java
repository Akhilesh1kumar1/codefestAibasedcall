package com.sr.capital.external.crif.controller;

import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class CrifController {

    final CrifPartnerService crifPartnerService;

    public CrifController(CrifPartnerService crifPartnerService) {
        this.crifPartnerService = crifPartnerService;
    }

    //REFACTORED & REVISITED
    @PostMapping(value = "/crif/stage1")
    public ResponseEntity<?> crifStage1(
    ) throws Exception {

        BureauQuestionnaireResponse bureauQuestionnaireResponse = crifPartnerService.initiateBureau(new BureauInitiatePayloadRequest());

        ResponseEntity<BureauQuestionnaireResponse> response = new ResponseEntity<>(bureauQuestionnaireResponse, HttpStatusCode.valueOf(200));

        return response;
    }

    @PostMapping(value = "/crif/stage2", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crifStage2(@RequestBody BureauInitiateResponse bureauInitiateResponse
                                        ) throws Exception {

        BureauQuestionnaireResponse questionnaire = crifPartnerService.getQuestionnaire(bureauInitiateResponse);
        ResponseEntity<BureauQuestionnaireResponse> response = new ResponseEntity<>(questionnaire, HttpStatusCode.valueOf(200));

        return response;

    }

    @PostMapping(value = "/crif/stage3", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crifStage3(@RequestBody BureauReportPayloadRequest bureauReportPayloadRequest
    ) throws Exception {

        BureauReportResponse report = crifPartnerService.getReport(bureauReportPayloadRequest);
        ResponseEntity<BureauReportResponse> response = new ResponseEntity<>(report, HttpStatusCode.valueOf(200));

        return response;

    }
}