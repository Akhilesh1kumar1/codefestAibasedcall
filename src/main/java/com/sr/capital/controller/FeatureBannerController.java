package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.banner.service.FeatureBannerService;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureBannerController {

    private final FeatureBannerService featureBannerService;

    @GetMapping("/banners")
    public GenericResponse<Map<String, Object>> getFeatureBanners(@RequestParam(name = "mobile", required = false) String mobile) throws IOException {
        return ResponseBuilderUtil.getResponse(featureBannerService.getFeatureBannerData(mobile), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
