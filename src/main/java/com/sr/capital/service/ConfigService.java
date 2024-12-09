package com.sr.capital.service;

import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.entity.mongo.FeatureDetails;


public interface ConfigService {

    public FeatureDetails getCompanyWithFeature();

    public FeatureDetails saveCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto);

    public FeatureDetails updateCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto);
}