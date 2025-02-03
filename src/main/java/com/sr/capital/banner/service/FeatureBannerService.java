package com.sr.capital.banner.service;

import com.sr.capital.banner.enums.BannerResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface FeatureBannerService {


    List<BannerResponse> getFeatureBannerData(String mobile) throws IOException;

    Map<String, Object> getPublicBannerData();
}
