package com.sr.capital.los.service;


import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.los.dto.LosUserDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface LosUserService {

    LosUserDTO getUserDetails(String token);
    IdSearchResponseDto<?> updateUser(LosUserDTO updatedUser) throws Exception;

    UUID generateOtp(String mobile) throws CustomException;

    Object verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception;
}
