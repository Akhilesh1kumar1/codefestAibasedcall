package com.sr.capital.banner.service;

import com.sr.capital.banner.enums.FeatureBannerEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeatureBannerServiceImpl implements FeatureBannerService {

    @Override
    public Map<String, Object> getFeatureBannerData() {
        Map<String, Object> response = new HashMap<>();

        for (FeatureBannerEnum feature : FeatureBannerEnum.values()) {
            response.put(feature.name(), feature.getBannerUrls());
        }

        return response;
    }
}