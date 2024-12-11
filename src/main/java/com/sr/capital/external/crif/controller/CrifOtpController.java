package com.sr.capital.external.crif.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.service.CrifOtpService;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.service.VerificationService;
import com.sr.capital.service.impl.VerificationUtilService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.VALID_PAN;

@RestController
@Validated
@RequestMapping("/api/crif")
@RequiredArgsConstructor
public class CrifOtpController {

    private final CrifPartnerService crifPartnerService;
    private final CrifOtpService crifOtpService;
    private final VerificationService verificationService;

    @PostMapping(value = "/generate-otp")
    public GenericResponse<?> crifGenerateOtp( @RequestBody CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) throws Exception {
        return ResponseBuilderUtil.getResponse(crifOtpService.generateOtp(crifGenerateOtpRequestModel), SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/verify-otp")
    public GenericResponse<Object> verifyOtp( @RequestBody CrifVerifyOtpRequestModels request) throws Exception {

            return ResponseBuilderUtil.getResponse(crifOtpService.verifyOtp(request), SUCCESS,
                    REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/update-otp-verification-status/{mobile}")
    public GenericResponse<?> updateOtpVerificationStatus(@PathVariable String mobile) throws Exception {


        crifOtpService.updateOtpStatus(mobile);

        return ResponseBuilderUtil.getResponse(null,
                SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);    }


}