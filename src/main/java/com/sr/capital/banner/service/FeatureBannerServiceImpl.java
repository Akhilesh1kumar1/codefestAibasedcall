package com.sr.capital.banner.service;

import com.sr.capital.banner.enums.BannerResponse;
import com.sr.capital.banner.enums.FeatureBannerEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeatureBannerServiceImpl implements FeatureBannerService {

    @Override
    public Map<String, Object> getFeatureBannerData() {
        List<BannerResponse> dtoList = FeatureBannerEnum.CRIF.toRisponseDtoList();
        return Map.of(FeatureBannerEnum.CRIF.name(), dtoList);
    }

    @Override
    public Map<String, Object> getPublicBannerData() {
        Map<String, Object> response = new HashMap<>();

        for (FeatureBannerEnum feature : FeatureBannerEnum.values()) {
            Map<String, String> bannerUrls = feature.getBannerUrls();
            response.put(feature.name(), bannerUrls.keySet());
        }

        return response;
    }
}