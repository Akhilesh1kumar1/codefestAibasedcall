package com.sr.capital.entity.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.sr.capital.helpers.constants.Constants.EntityNames.COMPANY_WISE_REPORT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = COMPANY_WISE_REPORT)
public class CompanyWiseReport {

    @Id
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "orders_ltv", nullable = false)
    private Long ordersLtv = 0L;

    @Column(name = "user_phone", length = 40)
    private String userPhone;

    @Column(name = "tier", length = 200)
    private String tier;

    @Column(name = "register_platform", columnDefinition = "text")
    private String registerPlatform;

    @Column(name = "last_shipment_date")
    private LocalDateTime lastShipmentDate;

    @Column(name = "unprocessed_order_ltv", nullable = false)
    private Long unprocessedOrderLtv = 0L;

    @Column(name = "orders_mtd", nullable = false)
    private Long ordersMtd = 0L;

    @Column(name = "company_ltv", nullable = false)
    private Long companyLtv = 0L;

    @Column(name = "first_shipment_date")
    private LocalDateTime firstShipmentDate;

    @Column(name = "cod_orders", nullable = false)
    private Long codOrders = 0L;

    @Column(name = "awb_shipped", nullable = false)
    private Long awbShipped = 0L;

    @Column(name = "first_recharge_date")
    private LocalDateTime firstRechargeDate;

    @Column(name = "last_recharge_date")
    private LocalDateTime lastRechargeDate;

    @Column(name = "success_recharge_amount", nullable = false, precision = 20, scale = 4)
    private BigDecimal successRechargeAmount = BigDecimal.ZERO;

    @Column(name = "awb_delivered", nullable = false)
    private Long awbDelivered = 0L;

    @Column(name = "first_order_date")
    private LocalDateTime firstOrderDate;

    @Column(name = "ftr_amount", nullable = false, precision = 20, scale = 4)
    private BigDecimal ftrAmount = BigDecimal.ZERO;

    @Column(name = "shipment_last_30_days", nullable = false)
    private Long shipmentLast30Days = 0L;

    @Column(name = "rto_percentage", nullable = false, precision = 20, scale = 4)
    private BigDecimal rtoPercentage = BigDecimal.ZERO;

    @Column(name = "total_success_recharge", nullable = false)
    private Long totalSuccessRecharge = 0L;

    @Column(name = "non_custom_active_channels", nullable = false)
    private Long nonCustomActiveChannels = 0L;

    @Column(name = "non_custom_total_channels", nullable = false)
    private Long nonCustomTotalChannels = 0L;

    @Column(name = "currently_in_pickup_schedule", nullable = false)
    private Long currentlyInPickupSchedule = 0L;

    @Column(name = "orders_crossed_expected_delivery_date", nullable = false)
    private Long ordersCrossedExpectedDeliveryDate = 0L;

    @Column(name = "return_order_count", nullable = false)
    private Long returnOrderCount = 0L;

    @Column(name = "first_ndr")
    private LocalDateTime firstNdr;

    @Column(name = "ndr_count", nullable = false)
    private Long ndrCount = 0L;

    @Column(name = "whmcs_client_id", length = 100)
    private String whmcsClientId;

    @Column(name = "register_utm_campaign", columnDefinition = "text")
    private String registerUtmCampaign;

    @Column(name = "register_utm_content", columnDefinition = "text")
    private String registerUtmContent;

    @Column(name = "register_utm_medium", columnDefinition = "text")
    private String registerUtmMedium;

    @Column(name = "register_utm_source", columnDefinition = "text")
    private String registerUtmSource;

    @Column(name = "register_utm_term", columnDefinition = "text")
    private String registerUtmTerm;

    @Column(name = "first_saas_plan_activation_date")
    private LocalDateTime firstSaasPlanActivationDate;

    @Column(name = "awb_rto", nullable = false)
    private Long awbRto = 0L;

    @Column(name = "prepaid_orders", nullable = false)
    private Long prepaidOrders = 0L;

    @Column(name = "shipments_count_last_month", nullable = false)
    private Long shipmentsCountLastMonth = 0L;

    @Column(name = "awb_created", nullable = false)
    private Long awbCreated = 0L;

    @Column(name = "company_tag", length = 100)
    private String companyTag;

    /*@Column(name = "first_rto_date")
    private LocalDateTime firstRtoDate;

    @Column(name = "total_ndr_awbs", nullable = false)
    private Long totalNdrAwbs = 0L;

    @Column(name = "total_weight_discrepancies", nullable = false)
    private Long totalWeightDiscrepancies = 0L;

    @Column(name = "first_weight_discrepancy_date")
    private LocalDateTime firstWeightDiscrepancyDate;

    @Column(name = "bank_details_updated", nullable = false)
    private Long bankDetailsUpdated = 0L;

    @Column(name = "training_attended", nullable = false)
    private Long trainingAttended = 0L;

    @Column(name = "first_order_delivery_date")
    private LocalDateTime firstOrderDeliveryDate;

    @Column(name = "first_pickup_failed_date")
    private LocalDateTime firstPickupFailedDate;

    @Column(name = "first_pickup_scheduled_date")
    private LocalDateTime firstPickupScheduledDate;

    @Column(name = "postship_enabled", nullable = false)
    private Long postshipEnabled = 0L;

    @Column(name = "brand_name_amazon")
    private Long brandNameAmazon;

    @Column(name = "brand_name_shopify")
    private Long brandNameShopify;

    @Column(name = "brand_name_woocoomerce")
    private Long brandNameWoocommerce;

    @Column(name = "brand_name_magento")
    private Long brandNameMagento;

    @Column(name = "successful_recharge_amount_mtd", precision = 20, scale = 4)
    private BigDecimal successfulRechargeAmountMtd;

    @Column(name = "is_az_channel")
    private Long isAzChannel;

    @Column(name = "highest_orders_in_month")
    private Long highestOrdersInMonth;

    @Column(name = "landing_page", columnDefinition = "text")
    private String landingPage;

    @Column(name = "onboarding_cod_orders", length = 355)
    private String onboardingCodOrders;

    @Column(name = "onboarding_has_website", length = 355)
    private String onboardingHasWebsite;

    @Column(name = "onboarding_sales_channel", length = 355)
    private String onboardingSalesChannel;

    @Column(name = "onboarding_shipment_count", length = 355)
    private String onboardingShipmentCount;

    @Column(name = "onboarding_product_type_type", length = 355, columnDefinition = "varchar(355) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String onboardingProductTypeType;

    @Column(name = "onboarding_business_type", length = 355)
    private String onboardingBusinessType;

    @Column(name = "onboarding_website", columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String onboardingWebsite;

    @Column(name = "current_plan", length = 200)
    private String currentPlan;

    @Column(name = "total_active_channels", nullable = false, columnDefinition = "bigint default 0")
    private Long totalActiveChannels;

    @Column(name = "shipment_m3", columnDefinition = "bigint")
    private Long shipmentM3;

    @Column(name = "shipment_m2", columnDefinition = "bigint")
    private Long shipmentM2;

    @Column(name = "shipment_m1", columnDefinition = "bigint")
    private Long shipmentM1;

    @Column(name = "shipment_m0", columnDefinition = "bigint")
    private Long shipmentM0;

    @Column(name = "current_usable_balance", nullable = false, columnDefinition = "decimal(20,4) default 0.0000")
    private BigDecimal currentUsableBalance;

    @Column(name = "current_total_balance", nullable = false, columnDefinition = "decimal(20,4) default 0.0000")
    private BigDecimal currentTotalBalance;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signupDate;

    @Column(name = "shipment_last_120_days_except_cancelled", columnDefinition = "bigint")
    private Long shipmentLast120DaysExceptCancelled;

    @Column(name = "orders_last_120_days", columnDefinition = "bigint")
    private Long ordersLast120Days;

    @Column(name = "seller_score", nullable = false, columnDefinition = "decimal(20,4) default 0.0000")
    private BigDecimal sellerScore;

    @Column(name = "seller_score_category", length = 255)
    private String sellerScoreCategory;

    @Column(name = "seller_score_updated_on")
    private LocalDateTime sellerScoreUpdatedOn;

    @Column(name = "pending_invoice_amount", columnDefinition = "decimal(20,4) default 0.0000")
    private BigDecimal pendingInvoiceAmount;

    @Column(name = "freight_unbilled_awb_count", columnDefinition = "bigint default 0")
    private Long freightUnbilledAwbCount;

    @Column(name = "last_recharge_amount", columnDefinition = "decimal(10,2) default 0.00")
    private BigDecimal lastRechargeAmount;

    @Column(name = "kyc_verified_date")
    private LocalDateTime kycVerifiedDate;

    @Column(name = "kyc_status", length = 30)
    private String kycStatus;

    @Column(name = "amount_to_be_remitted", columnDefinition = "decimal(10,2)")
    private BigDecimal amountToBeRemitted;*/


}
