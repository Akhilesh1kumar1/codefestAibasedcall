package com.sr.capital.controller;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.dto.response.FeatureDetailResponseDto;
import com.sr.capital.service.ConfigService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;


@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
@Validated
public class ConfigController {

    final ConfigService configService;

    @GetMapping("")
    public GenericResponse<FeatureDetailResponseDto> getFeatureDetails(){
        return ResponseBuilderUtil.getResponse(configService.getCompanyWithFeature(),SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @PostMapping("")
    public GenericResponse<FeatureDetailResponseDto> saveFeatureDetails(@RequestBody List<FeatureDetailRequestDto> featureDetailRequestDto){
        return ResponseBuilderUtil.getResponse(configService.saveCompanyWithFeature(featureDetailRequestDto),SUCCESS,"",HttpStatus.SC_OK);
    }

    @PutMapping("")
    public GenericResponse<FeatureDetailResponseDto> updateFeatureDetails(@RequestBody FeatureDetailRequestDto featureDetailRequestDto){
        return ResponseBuilderUtil.getResponse(configService.updateCompanyWithFeature(featureDetailRequestDto),SUCCESS,"",HttpStatus.SC_OK);
    }

}
