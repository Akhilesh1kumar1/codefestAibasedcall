package com.sr.capital.los.controller;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.los.dto.LosUserDTO;
import com.sr.capital.los.dto.ScreenNameDTO;
import com.sr.capital.los.service.LosUserService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/los/users")
@RequiredArgsConstructor
public class LosController {

    private final LosUserService losUserService;

    // GET user by ID
    @GetMapping()
    public GenericResponse<LosUserDTO> getUserByToken(@RequestHeader(name = Constants.Headers.TOKEN) String token) {
        return ResponseBuilderUtil.getResponse(losUserService.getUserDetails(token)
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }
    @GetMapping("/get-data")
    public GenericResponse<ScreenNameDTO> fetchData(@RequestHeader(name = Constants.Headers.TOKEN) String token) throws Exception {
        return ResponseBuilderUtil.getResponse(losUserService.getScreenData(token)
                ,SUCCESS, REQUEST_SUCCESS, 200);
    }


    @PostMapping()
    public GenericResponse<ScreenNameDTO> updateUser(@RequestBody LosUserDTO updatedUser) throws Exception {
            return ResponseBuilderUtil.getResponse(losUserService.saveDetailes(updatedUser)
                    ,SUCCESS, REQUEST_SUCCESS, 200);
    }

    @PostMapping(value = "/generate-otp")
    public GenericResponse<UUID> crifGenerateOtp(@RequestBody String mobile) throws CRIFApiException, IOException, CustomException, CRIFApiLimitExceededException {
        return ResponseBuilderUtil.getResponse(losUserService.generateOtp(mobile), SUCCESS, REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping(value = "/verify-otp")
    public GenericResponse<Object> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) throws Exception {
        return ResponseBuilderUtil.getResponse(losUserService.verifyOtp(verifyOtpRequest), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

}
