package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.entity.mongo.FeatureDetails;
import com.sr.capital.repository.mongo.FeatureDetailRepository;
import com.sr.capital.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    final FeatureDetailRepository featureDetailRepository;
    @Override
    public FeatureDetails getCompanyWithFeature() {
        return featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));

    }

    @Override
    public FeatureDetails saveCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto){
        FeatureDetails featureDetails = featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));
        featureDetails.setFeature(featureDetailRequestDto.getFeature());
        featureDetailRepository.save(featureDetails);
        return featureDetails;
    }

    @Override
    public FeatureDetails updateCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto){
        FeatureDetails featureDetails = featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));
        List<String> newFeatures = featureDetailRequestDto.getFeature();
        List<String> existingFeatures = Optional.ofNullable(featureDetails.getFeature())
                .orElseGet(ArrayList::new);
        existingFeatures.addAll(
                newFeatures.stream()
                        .filter(feature -> feature != null && !feature.isBlank())
                        .toList()
        );
        featureDetails.setFeature(existingFeatures);
        featureDetails = featureDetailRepository.save(featureDetails);
        return featureDetails;
    }

}
