package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.FeatureDetails;
import com.sr.capital.repository.mongo.FeatureDetailRepository;
import com.sr.capital.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    final FeatureDetailRepository featureDetailRepository;
    @Override
    public FeatureDetails getCompanyWithFeature() {
        FeatureDetails featureDetails = featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));
        return featureDetails;
    }
}
