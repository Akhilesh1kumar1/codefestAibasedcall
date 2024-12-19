package com.sr.capital.banner.enums;

import java.util.Arrays;
import java.util.List;

public enum FeatureBannerEnum {

    CRIF(Arrays.asList("/banner1.jpg", "/banner2.jpg"));
    private final List<String> bannerUrls;

    FeatureBannerEnum(List<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    public List<String> getBannerUrls() {
        return bannerUrls;
    }
}
