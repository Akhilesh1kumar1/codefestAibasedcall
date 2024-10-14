package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.ResendOtpRequest;
import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.VerificationService;
import com.sr.capital.util.ResponseBuilderUtil;
import com.sr.capital.validation.MobileNumberValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.VALID_PAN;

@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
@Validated
public class VerificationController {

    final VerificationService verificationService;
    @PostMapping("/verify-pan")
    public GenericResponse<Boolean> verifyPan(@RequestParam("pan")String pan) throws Exception {

       return ResponseBuilderUtil.getResponse( verificationService.verifyPan(pan, RequestType.PAN),SUCCESS,
               VALID_PAN, HttpStatus.SC_OK);

    }

    @PostMapping(path = "/verify-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<Boolean> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) throws Exception {
        return ResponseBuilderUtil.getResponse( verificationService.verifyOtp(request),SUCCESS,
                VALID_PAN, HttpStatus.SC_OK);
    }

    @PostMapping(path = "/verify-gst", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<Boolean> verifyGst(@RequestParam("gst")String gst) throws Exception {
        return ResponseBuilderUtil.getResponse( verificationService.verifyGst(gst),SUCCESS,
                VALID_PAN, HttpStatus.SC_OK);
    }

    @PostMapping(path = "/resend-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<Boolean> resendOtp(@Valid @RequestBody ResendOtpRequest request) throws Exception {
        return ResponseBuilderUtil.getResponse(verificationService.resendOtp(request),SUCCESS,
                "", HttpStatus.SC_OK);
    }

}
