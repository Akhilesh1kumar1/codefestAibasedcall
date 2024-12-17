package com.sr.capital.external.crif.service;

import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.dto.response.CrifUserDetailsResponseDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CrifOtpService {

    CrifResponse generateOtp(CrifGenerateOtpRequestModel bureauInitiatePayloadRequest) throws IOException;

    CrifResponse verifyOtp(@Valid CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) throws CustomException;

    void updateOtpStatus(String mobile);

    CrifUserDetailsResponseDto getUserDetails(String token);
}
