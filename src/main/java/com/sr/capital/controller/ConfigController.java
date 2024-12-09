package com.sr.capital.controller;


import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.entity.mongo.FeatureDetails;
import com.sr.capital.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
@Validated
public class ConfigController {

    final ConfigService configService;

    @GetMapping("/get")
    public FeatureDetails getFeatureDetails(){
        return configService.getCompanyWithFeature();
    }

    @PostMapping("/save")
    public FeatureDetails saveFeatureDetails(@RequestBody FeatureDetailRequestDto featureDetailRequestDto){
        return configService.saveCompanyWithFeature(featureDetailRequestDto);
    }

    @PutMapping("/update")
    public FeatureDetails updateFeatureDetails(@RequestBody FeatureDetailRequestDto featureDetailRequestDto){
        return configService.updateCompanyWithFeature(featureDetailRequestDto);
    }




}
