package com.sr.capital.controller;



import com.sr.capital.entity.mongo.FeatureDetails;
import com.sr.capital.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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



}
