package com.sr.capital.service;

import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.dto.response.FeatureDetailResponseDto;

import java.util.List;


public interface ConfigService {

    FeatureDetailResponseDto getCompanyWithFeature();

    FeatureDetailResponseDto saveCompanyWithFeature(List<FeatureDetailRequestDto> featureDetailRequestDto);

    FeatureDetailResponseDto updateCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto);
}