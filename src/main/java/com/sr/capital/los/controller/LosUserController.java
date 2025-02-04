package com.sr.capital.los.controller;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.los.dto.LosUserDTO;
import com.sr.capital.los.service.LosUserService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/los/users")
@RequiredArgsConstructor
public class LosUserController {

    private final LosUserService losUserService;

    // GET user by ID
    @GetMapping()
    public GenericResponse<LosUserDTO> getUserByToken(@RequestHeader(name = Constants.Headers.TOKEN) String token) {
        return ResponseBuilderUtil.getResponse(losUserService.getUserDetails(token)
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }


    // PUT (Update User)
    @PostMapping()
    public GenericResponse<IdSearchResponseDto<?>> updateUser(@RequestBody LosUserDTO updatedUser) throws Exception {
            return ResponseBuilderUtil.getResponse(losUserService.updateUser(updatedUser)
                    ,SUCCESS, REQUEST_SUCCESS, 200);
    }

    @PostMapping(value = "/generate-otp")
    public GenericResponse<VerificationOrchestratorRequest> crifGenerateOtp(@RequestBody String mobile) throws CRIFApiException, IOException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(losUserService.generateOtp(mobile), SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/verify-otp")
    public GenericResponse<Object> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) throws Exception {
        return ResponseBuilderUtil.getResponse(losUserService.verifyOtp(verifyOtpRequest), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

}
