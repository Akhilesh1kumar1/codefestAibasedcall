package com.sr.capital.external.crif.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.BureauReportPayloadRequest;
import com.sr.capital.external.crif.dto.request.CrifConsentWithdrawalRequestModel;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.dto.response.CrifICRMReportRdo;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.util.Base64Util;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@Validated
@RequestMapping("/api/crif")
@RequiredArgsConstructor
public class CrifController {

    final CrifPartnerService crifPartnerService;

    @PostMapping(value = "/initiate")
    public GenericResponse<?> crifStage1(@RequestBody BureauInitiatePayloadRequest bureauInitiatePayloadRequest
    ) throws Exception {
        return ResponseBuilderUtil.getResponse(crifPartnerService.initiateBureau(bureauInitiatePayloadRequest)
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }

    @PostMapping(value = "/validate", consumes = "application/json", produces = "application/json")
    public GenericResponse<?> crifStage2(@RequestBody BureauInitiateResponse bureauInitiateResponse) throws CRIFApiException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(crifPartnerService.verify(bureauInitiateResponse),
                SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/report", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crifStage3(@RequestBody BureauReportPayloadRequest bureauReportPayloadRequest
    ) throws CRIFApiException, CRIFApiLimitExceededException {

        BureauReportResponse report = crifPartnerService.getReport(bureauReportPayloadRequest, false);
        ResponseEntity<BureauReportResponse> response = new ResponseEntity<>(report, HttpStatusCode.valueOf(200));

        return response;

    }

    @PostMapping(value = "/purge", consumes = "application/json", produces = "application/json")
    public GenericResponse<Map<String, String>> crifConsentWithdrawal(@RequestBody CrifConsentWithdrawalRequestModel crifConsentWithdrawalRequestModel
    ) throws CRIFApiException {

        crifPartnerService.consentWithdrawalProcess(crifConsentWithdrawalRequestModel);
        return ResponseBuilderUtil.getResponse(null
                ,SUCCESS, REQUEST_SUCCESS, 200);

    }



    @PostMapping(value = "/getAccessCode")
    public ResponseEntity<?> getAccessCode() {

        String accessCode = crifPartnerService.getAccessCode();

        return new ResponseEntity<>(new String[]{accessCode, Base64Util.decode(accessCode)}, HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/get-report")
    public GenericResponse<Page<CrifICRMReportRdo>> getReport(@RequestParam(name = "page", required = false) Integer pageNumber,
                                                              @RequestParam(name = "size", required = false) Integer pageSize,
                                                              @RequestParam(name = "mobile", required = false) String mobile,
                                                              @RequestParam(name = "date_of_initiation", required = false) Date dateOfInitiation,
                                                              @RequestParam(name = "score", required = false) String score,
                                                              @RequestParam(name = "company_id", required = false) String companyId) {

        return ResponseBuilderUtil.getResponse(crifPartnerService.getICRMReport(pageNumber, pageSize, mobile, companyId, dateOfInitiation, score)
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }

    @GetMapping(value = "/get-doc-type")
    public GenericResponse<?> getDocType() {
        return ResponseBuilderUtil.getResponse(crifPartnerService.getDocType()
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }




}