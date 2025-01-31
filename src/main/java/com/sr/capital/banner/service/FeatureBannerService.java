package com.sr.capital.banner.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public interface FeatureBannerService {


    Map<String, Object> getFeatureBannerData(String mobile) throws IOException;

    Map<String, Object> getPublicBannerData();
}
