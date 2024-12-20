package com.sr.capital.banner.enums;

import java.util.Arrays;
import java.util.List;

public enum FeatureBannerEnum {

    CRIF(Arrays.asList("img/Dtdc.png", "img/professional-logistics.png", "img/track_bg.png"));
    private final List<String> bannerUrls;

    FeatureBannerEnum(List<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    public List<String> getBannerUrls() {
        return bannerUrls;
    }
}
