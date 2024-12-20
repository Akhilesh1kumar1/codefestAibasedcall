package com.sr.capital.service;

import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.dto.response.FeatureDetailResponseDto;



public interface ConfigService {

    FeatureDetailResponseDto getCompanyWithFeature();

    FeatureDetailResponseDto saveCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto);

    FeatureDetailResponseDto updateCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto);
}