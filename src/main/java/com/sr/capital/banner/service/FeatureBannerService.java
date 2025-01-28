package com.sr.capital.banner.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public interface FeatureBannerService {


    Map<String, Object> getFeatureBannerData();

    Map<String, Object> getPublicBannerData();
}
