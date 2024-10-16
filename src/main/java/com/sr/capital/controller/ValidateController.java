package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.ValidateService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.VALID_MOBILE_NUMBER;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.VALID_PAN;

@RestController
@RequestMapping("/api/validate")
@RequiredArgsConstructor
@Validated
public class ValidateController {

    final ValidateService validateService;

    @PostMapping("/pan")
    public GenericResponse<Boolean> verifyPan(@RequestParam("pan")String pan) throws Exception {

        return ResponseBuilderUtil.getResponse( validateService.validatePan(pan),SUCCESS,
                VALID_PAN, HttpStatus.SC_OK);

    }


    @PostMapping(path = "/gst", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse<Boolean> verifyGst(@RequestParam("gst")String gst) throws Exception {
        return ResponseBuilderUtil.getResponse( validateService.validateGst(gst),SUCCESS,
                VALID_PAN, HttpStatus.SC_OK);
    }

    @PostMapping("/mobile")
    public GenericResponse<Boolean> verifyMobile(@Valid @RequestBody ValidateMobileNumberRequestDto validateMobileNumberRequestDto) throws Exception {
        return ResponseBuilderUtil.getResponse( validateService.validateMobileNumber(validateMobileNumberRequestDto),SUCCESS,
                VALID_MOBILE_NUMBER, HttpStatus.SC_OK);
    }
}
