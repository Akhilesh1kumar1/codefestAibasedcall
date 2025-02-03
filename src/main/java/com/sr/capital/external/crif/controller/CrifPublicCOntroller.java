package com.sr.capital.external.crif.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.banner.service.FeatureBannerService;
import com.sr.capital.dto.request.ResendOtpRequest;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.CrifConsentWithdrawalRequestModel;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.external.crif.service.CrifOtpService;
import com.sr.capital.external.crif.service.CrifPartnerService;

import com.sr.capital.service.VerificationService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@Validated
@RequestMapping("/api/public/crif")
@RequiredArgsConstructor
public class CrifPublicCOntroller {

    private final CrifOtpService crifOtpService;
    private final CrifPartnerService crifPartnerService;
    private final VerificationService verificationService;
    private final FeatureBannerService featureBannerService;

    @PostMapping(value = "/generate-otp")
    public GenericResponse<?> crifGenerateOtp(@RequestBody CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) throws CRIFApiException, IOException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(crifOtpService.generateOtp(crifGenerateOtpRequestModel), SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/verify-otp")
    public GenericResponse<Object> verifyOtp(@RequestBody CrifVerifyOtpRequestModels request) throws CRIFApiException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(crifOtpService.verifyOtp(request), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/purge", consumes = "application/json", produces = "application/json")
    public GenericResponse<Map<String, String>> crifConsentWithdrawal(@RequestBody CrifConsentWithdrawalRequestModel crifConsentWithdrawalRequestModel
    ) throws CRIFApiException {
        crifPartnerService.consentWithdrawalProcess(crifConsentWithdrawalRequestModel);
        return ResponseBuilderUtil.getResponse(null
                ,SUCCESS, REQUEST_SUCCESS, 200);

    }

    @PostMapping(value = "/validate", consumes = "application/json", produces = "application/json")
    public GenericResponse<?> crifStage2(@RequestBody BureauInitiateResponse bureauInitiateResponse) throws CRIFApiException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(crifPartnerService.verify(bureauInitiateResponse),
                SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping(value = "/get-doc-type")
    public GenericResponse<?> getDocType() {
        return ResponseBuilderUtil.getResponse(crifPartnerService.getDocType()
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }

    @PostMapping(path = "/resend-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<Boolean> resendOtp(@Valid @RequestBody ResendOtpRequest request) throws Exception {
        return ResponseBuilderUtil.getResponse(verificationService.resendOtp(request),SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @GetMapping("/banners")
    public GenericResponse<Map<String, Object>> getFeatureBanners() {
        return ResponseBuilderUtil.getResponse(featureBannerService.getPublicBannerData(), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
