package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.FeatureDetailRequestDto;
import com.sr.capital.dto.response.FeatureDetailResponseDto;
import com.sr.capital.entity.mongo.FeatureDetails;
import com.sr.capital.repository.mongo.FeatureDetailRepository;
import com.sr.capital.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    final FeatureDetailRepository featureDetailRepository;

    @Override
    public FeatureDetailResponseDto getCompanyWithFeature() {
        FeatureDetails featureDetails = featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));
        return FeatureDetailResponseDto.builder()
                .srCompanyId(featureDetails.getSrCompanyId())
                .feature(featureDetails.getFeature())
                .build();
    }

    @Override
    public FeatureDetailResponseDto saveCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto){
        FeatureDetails featureDetails = featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));
        List<String> cleanedFeatures = featureDetailRequestDto.getFeature().stream()
                .filter(feature -> feature != null && !feature.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        featureDetails.setFeature(cleanedFeatures);
        featureDetailRepository.save(featureDetails);
        return FeatureDetailResponseDto.builder()
                .srCompanyId(featureDetails.getSrCompanyId())
                .feature(featureDetails.getFeature())
                .build();
    }

    @Override
    public FeatureDetailResponseDto updateCompanyWithFeature(FeatureDetailRequestDto featureDetailRequestDto){
        FeatureDetails featureDetails = featureDetailRepository.findBySrCompanyId(Long.parseLong(RequestData.getTenantId()));

        List<String> existingFeatures = Optional.ofNullable(featureDetails.getFeature()).orElseGet(ArrayList::new);
        Set<String> existingFeatureSet = new HashSet<>(existingFeatures);

        List<String> newFeatures = Optional.ofNullable(featureDetailRequestDto.getFeature()).orElseGet(Collections::emptyList);

        newFeatures.stream()
                .filter(feature -> feature != null && !feature.isBlank() && existingFeatureSet.add(feature))
                .forEach(existingFeatures::add);

        featureDetails.setFeature(existingFeatures);
        featureDetails = featureDetailRepository.save(featureDetails);

        return FeatureDetailResponseDto.builder()
                .srCompanyId(featureDetails.getSrCompanyId())
                .feature(featureDetails.getFeature())
                .build();
    }

}
