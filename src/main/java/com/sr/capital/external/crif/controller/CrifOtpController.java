package com.sr.capital.external.crif.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.external.crif.service.CrifOtpService;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@Validated
@RequestMapping("/api/crif")
@RequiredArgsConstructor
public class CrifOtpController {

    private final CrifOtpService crifOtpService;

    @PostMapping(value = "/generate-otp")
    public GenericResponse<?> crifGenerateOtp( @RequestBody CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) throws CRIFApiException, IOException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(crifOtpService.generateOtp(crifGenerateOtpRequestModel), SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/verify-otp")
    public GenericResponse<Object> verifyOtp(@RequestBody CrifVerifyOtpRequestModels request) throws CRIFApiException, CustomException, CRIFApiLimitExceededException {

            return ResponseBuilderUtil.getResponse(crifOtpService.verifyOtp(request), SUCCESS,
                    REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/update-otp-verification-status/{mobile}")
    public GenericResponse<?> updateOtpVerificationStatus(@PathVariable String mobile)  {


        crifOtpService.updateOtpStatus(mobile);

        return ResponseBuilderUtil.getResponse(null,
                SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping(value = "/user-details")
    public GenericResponse<?> getUserDetails(@RequestHeader(name = Constants.Headers.TOKEN) String token)  {
        return ResponseBuilderUtil.getResponse(crifOtpService.getUserDetails(token)
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }


}