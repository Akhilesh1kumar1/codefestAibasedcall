package com.sr.capital.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BannerResponse {
    private String image;
    private Object redirectUrl;
}