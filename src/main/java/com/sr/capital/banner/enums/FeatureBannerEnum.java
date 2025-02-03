package com.sr.capital.banner.enums;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum FeatureBannerEnum {

    CRIF(Map.of("img/Dtdc.png", "https://google.com/",
            "img/professional-logistics.png", "https://www.youtube.com/",
            "img/track_bg.png", "https://chatgpt.com/",
            "agreements/17309752929pkE6.png",  "https://chat.deepseek.com/",
            "agreements/1730450782ooOZ5.png", "https://shiprocket.ssoone.com/",
            "agreements/1725954795IIOJj.png", "https://mail.google.com/"));
    private final Map<String, String> bannerMap;

    FeatureBannerEnum(Map<String, String> bannerMap) {
        this.bannerMap = bannerMap;
    }

    public Map<String, String> getBannerUrls() {
        return bannerMap;
    }

    public List<BannerResponse> toRisponseDtoList() {
        return bannerMap.entrySet().stream()
                .map(entry -> new BannerResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
