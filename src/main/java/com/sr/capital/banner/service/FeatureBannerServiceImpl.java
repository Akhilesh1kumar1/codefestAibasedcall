package com.sr.capital.banner.service;

import com.sr.capital.banner.enums.BannerResponse;
import com.sr.capital.banner.enums.FeatureBannerEnum;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.MonthlySalesDetails;
import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.repository.mongo.CrifReportRepo;
import com.sr.capital.service.CapitalDataReportService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class FeatureBannerServiceImpl implements FeatureBannerService {

    private final CapitalDataReportService capitalDataReportService;
    private final CrifReportRepo crifReportRepo;
    private final AppProperties appProperties;
    @Override
    public Map<String, Object> getFeatureBannerData(String mobile) throws IOException {

        Map<String, Object> response = new HashMap<>();
        // Todo :: Update after geting clarity on gmv values
        if (mobile != null) {
            Optional<CrifReport> optional = crifReportRepo.findByMobile(mobile);
            if (optional.isPresent()) {
                CrifReport crifReport = optional.get();
                String crifScore = crifReport.getCrifScore();
                if (crifScore != null) {
                    if (Long.parseLong(crifScore) >= appProperties.getCrifScoreThreshold()) {
                        List<CompanySalesDetails> companySalesDetails = capitalDataReportService.getCompanySalesDetails(Long.valueOf(RequestData.getTenantId()));
                        if (CollectionUtils.isNotEmpty(companySalesDetails)) {
                            CompanySalesDetails companySalesDetails1 = companySalesDetails.get(0);
                            Map<String, MonthlySalesDetails> detailsInfo = companySalesDetails1.getDetailsInfo();
                            Collection<MonthlySalesDetails> values = detailsInfo.values();
                            long shipmentGmv = values.stream().map(MonthlySalesDetails::getShipmentGmv).mapToLong(Long::longValue).sum();
                            long avgShipmentGmv = shipmentGmv / values.size();
                            if (avgShipmentGmv > appProperties.getCrifShipmentGMVThreshold()) {
                                //handle gmv cases
                            }
                        }
                    }
                }
            }

        }for (FeatureBannerEnum feature : FeatureBannerEnum.values()) {
            response.put(feature.name(), feature.getBannerUrls());
        }

        return response;
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