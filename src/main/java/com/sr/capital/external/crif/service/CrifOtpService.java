package com.sr.capital.external.crif.service;

import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CrifOtpService {

    CrifResponse generateOtp(CrifGenerateOtpRequestModel bureauInitiatePayloadRequest) throws IOException;

    CrifResponse verifyOtp(@Valid CrifVerifyOtpRequestModels crifGenerateOtpRequestModel);

    void updateOtpStatus(String mobile);

    Object getUserDetails(String token);
}
