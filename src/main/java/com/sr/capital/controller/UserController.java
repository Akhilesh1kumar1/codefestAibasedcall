package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.ValidateMobileResponse;
import com.sr.capital.service.UserService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.Headers.TOKEN;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    final UserService userService;
    @GetMapping("/external/details")
    public GenericResponse<ApiTokenUserDetailsResponse> getExternalUserDetails(@RequestHeader(name = TOKEN) String token){
        return ResponseBuilderUtil.getResponse(userService.getUserDetails(token),SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @GetMapping("/details")
    public GenericResponse<InternalTokenUserDetailsResponse> getInternalUserDetails(@RequestHeader(name = TOKEN) String token){
        return ResponseBuilderUtil.getResponse(userService.getUserDetailsUsingInternalToken(token),SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @PostMapping("/save")
    public GenericResponse<UserDetails> getUserDetails(@RequestBody UserDetails userDetails) throws CustomException {
        return ResponseBuilderUtil.getResponse(userService.saveUserDetails(userDetails),SUCCESS,
                "", HttpStatus.SC_OK);
    }


}
