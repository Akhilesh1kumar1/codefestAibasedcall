package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCompanyDetails {

    private Data data;
    private UserData userData;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Data {

        private int companyId;
        private String website;
        private boolean kyc;
        private boolean shopifyConnected;
        private String channelConnected;
        private String brandName;
        private String signupUtm;
        private String firstRechargeDate;
        private String firstRechargeAmount;
        private String plan;
        private String tier;
        private String lastShipmentDate;
        private String lastRechargeAmount;
        private String lastMonthOrders;
        private String lastDayOrders;
        private int skus;
        private String rtoPercentage;
        private String codPercentage;
        private boolean engage;
        private boolean allowMps;
        private int international;
        private int wdCount;
        private String registerUtmCampaign;
        private String firstNdr;
        private int ndrCount;
        private int totalNdrAwbs;
        private boolean brandBoost;
        private boolean deliveryBoost;
        private String pickupCity;
        private int companyShipInternationally;
        private String walletBalance;
        private int totalRechargeCount;
        private String totalRechargeAmount;
        private String firstShipmentDate;
        private String firstOrderDate;
        private String firstOrderShippedDate;
        private int companyAwbAssignedCount;
        private String companyOrderCount;
        private int companyOrderMonthly;
        private String utmSource;
        private String utmMedium;
        private String utmCampaign;
        private String utmTerm;
        private String utmContent;
        private int rechargeUsingCoupon;
        private String lastOrderShipped;
        private String registrationDevice;
        private boolean companyDetailsFilled;
        private boolean companyOtpVerified;
        private String accountType;
        private boolean companyGst;
        private String kycVerificationType;
        private boolean iec;
        private boolean isCommercialOrder;
        private String managerName;
        private String managerPhone;

    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UserData{
       private String platform;
    }
}
