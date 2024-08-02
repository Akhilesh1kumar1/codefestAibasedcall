package com.sr.capital.offer.calculator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferCalculatorRequestDto {

    Integer avgNumberOfShipmentsMonthly;

    Double rtoPercentageInLastSixMonth;

    String srPlan;

    BigDecimal avgNinetyDaysTotalBalance;

    BigDecimal weightScore;

    Double personalCibilScore;

    Double commercialCibilScore;

    Boolean itrFilledOnTime;

    BigDecimal itrScore;


    BigDecimal netRevenue;

    String gstRiskCheck;

    String complianceCheck; //Should be paid timely; check according to range in no of delay days; <15, 15-30, >30?

    Double lifeTimeOfCompliances;

    String recurringBusiness;// Check if amazon/flipkart/shopify in vendors?

    //ITR DATA

    Double receivableTurnOverRatio;

    Double payableTurnOverRatio;

    Double assetsTurnOverRatio;

    Double cashConversionRatio;

    private Double currentRatio;
    private Double quickRatio;
    private Double debtEquity;
    private Double debtCapital;
    private Double cashRatio;
    private Double returnOnAssets;
    private Double interestCoverageRatio;
    private Double badDebtsGrossRevenue;
    private Double numberOfRecurringClients;
    private Double badLoans;
    private Double currentOverdue;

    private Double netProfitMargin;
    private Double operatingProfitMargin;
    private Double avgMarginPerMonthMonthlyEmiPlus15K;

    private Double avgMonthlyInvoiceValue;
    private Double numberOfDirectors;

    private Boolean itrFilingOnTime;


    //BANK ANALYZER

    Boolean isRedFlag;

    BigDecimal avgBalance;

    Double noOFNegativeBalance;

    BigDecimal currentDPD;

    //BalanceSheet


    BigDecimal profitInLastOneYear;

    BigDecimal presenceOFLongTermBorrowing;

    BigDecimal spendOnMarketingExpenditure;

    Double ebitda; //DEBT

    Double dscr;

    Double leverage;


    //Sales Data

    Boolean presenceOnMultiplePlatform;

    BigDecimal growthInSales;

    BigDecimal steadyRevenue;

    BigDecimal growthInSalesComparedToTwelveMonths;


    //Inventory

    BigDecimal inventoryTurnOverData;






}
