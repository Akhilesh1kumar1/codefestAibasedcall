package com.sr.capital.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppProperties {

    //aws
    @Value("${aws.region}")
    String awsRegion;

    @Value("${aws.s3.bucketName}")
    String bucketName;

    @Value("${aws.cdn.url}")
    String cdnUrl;

    @Value("${fileUpload.coolOffWindowInMinutes}")
    int fileUploadCoolOffWindow;


    //external

    //SINFINI
    @Value("${app.external.sinfini.enabled}")
    private Boolean sinfiniCommunicationEnabled;

    @Value("${app.external.sinfini.baseUri}")
    private String sinfiniBaseurl;

    @Value("${app.external.sinfini.apiKey}")
    private String sinfiniApiKey;

    @Value("${app.external.sinfini.sms.sender}")
    private String sinfiniSmsSender;

    @Value("${app.external.sinfini.sms.method}")
    private String sinfiniSmsMethod;

    @Value("${app.external.sinfini.sms.templateId}")
    private String sinfiniSmsTemplateId;

    //KALEYRA
    @Value("${app.external.kaleyra.enabled}")
    private Boolean kaleyraCommunicationEnabled;

    @Value("${app.external.kaleyra.baseUri}")
    private String kaleyraBaseurl;

    @Value("${app.external.kaleyra.senderId}")
    private String kaleyraSenderId;

    @Value("${app.external.kaleyra.apiKey}")
    private String kaleyraApiKey;

    @Value("${app.external.kaleyra.fromNo}")
    private String kaleyraFromNo;

    @Value("${app.external.kaleyra.endpoints.send-messages}")
    private String kaleyraSendMessageEndPoint;

    @Value("${app.external.kaleyra.whatsapp.otp-template-name}")
    private String kaleyraWhatsappOtpTemplateName;

    @Value("${app.external.kaleyra.whatsapp.lead-not-connected-template-name}")
    private String kaleyraWhatsappSellerNotConnectedTemplateName;

    @Value("${app.external.kaleyra.whatsapp.seller-thanks-template-name}")
    private String kaleyraWhatsappSellerThanksTemplateName;

    @Value("${app.external.kaleyra.whatsapp.channel}")
    private String kaleyraWhatsappChannel;

    @Value("${app.external.kaleyra.whatsapp.callBackUri}")
    private String kaleyraWhatsappCallbackUri;

    //NETCORE
    @Value("${app.external.netcore.enabled}")
    private Boolean netCoreCommunicationEnabled;

    @Value("${app.external.netcore.baseUri}")
    private String netCoreBaseUri;

    @Value("${app.external.netcore.apiKey}")
    private String netCoreApiKey;

    @Value("${app.external.netcore.fromEmail}")
    private String netCoreFromEmail;

    @Value("${app.external.netcore.fromName}")
    private String netCoreFromName;

    @Value("${app.external.netcore.endpoints.send-mail}")
    private String netCoreSendEmailEndpoint;


    //Shiprocket
    @Value("${app.external.shiprocket.auth.baseUrl}")
    private String shiprocketAuthBaseUrl;

    @Value("${app.external.shiprocket.auth.endpoints.validate-token}")
    private String shiprocketValidateTokenEndPoint;

    @Value("${app.external.shiprocket.api.baseUrl}")
    private String shiprocketApiBaseUrl;

    @Value("${app.external.shiprocket.api.endpoints.kyc-details}")
    private String shiprocketKycEndPoint;


    @Value("${app.external.shiprocket.api.endpoints.user-details-api-token}")
    private String shiprocketApiTokenUserDetailsEndpoint;

    @Value("${app.external.shiprocket.api.endpoints.user-details-internal-token}")
    private String shiprocketInternalTokenUserDetailsEndpoint;
    //Verification Properties

    @Value("${app.verification.otp.failure-limit}")
    private Integer otpMaxFailureCount;

    @Value("${app.verification.otp.request-limit}")
    private Integer otpMaxRequestCount;

    @Value("${app.cache.password-expiry-in-minutes}")
    private Long updatePasswordCacheExpiry;

    @Value("${app.cache.sr-token-expiry-in-hours}")
    private Long srTokenCacheExpiry;


    //Encryption Properties
    @Value("${app.crypto.encryption.rsa.enabled}")
    private Boolean rsaEncryptionEnabled;

    @Value("${app.crypto.encryption.aes.key}")
    private String aesEncryptionKey;

    @Value("${app.secret}")
    private String appSecret;

    @Value("${spring.profiles.active}")
    private String activeProfile;



    //kyc

    // Expiry

    @Value("${kyc.aadhaar.verified.details.expiry.min:120}")
    private Long aadhaarVerifiedDetailsExpiry;

    @Value("${kyc.bank.verified.details.expiry.min:120}")
    private Long bankVerifiedDetailsExpiry;

    @Value("${kyc.gst.verified.details.expiry.min:120}")
    private Long gstVerifiedDetailsExpiry;

    @Value("${kyc.pan.aadhaar.cross.verified.details.expiry.min:120}")
    private Long panAadhaarCrossVerifiedDetailsExpiry;

    @Value("${kyc.pan.gst.cross.verified.details.expiry.min:120}")
    private Long panGstCrossVerifiedDetailsExpiry;

    @Value("${kyc.pan.verified.details.expiry.min:120}")
    private Long panVerifiedDetailsExpiry;

    /*
        karza Credential
     */

    @Value("${external.karza.account.id}")
    private String karzaAccountId;

    @Value("${external.karza.api.key}")
    private String karzaApiKey;

    @Value("${external.karza.enabled}")
    private Boolean karzaEnabled;

    @Value("${external.karza.vendor-token}")
    private String karzaVendorToken;

    @Value("${external.karza.vendor-code}")
    private String karzaVendorCode;

    /*
        karza EXTERNAL API
     */

    @Value("${external.karza.base.uri}")
    private String karzaBaseUri;

    @Value("${external.karza.api.endpoint.extract.gst.pan.url}")
    private String karzaExtractGSTDetailsByPanEndpoint;

    @Value("${external.karza.api.endpoint.verify.gst.otp-url}")
    private String karzaVerifyGSTOtpEndpoint;


    @Value("${external.karza.async.api.endpoint.tasks}")
    private String karzaAsyncTaskEndpoint;

    @Value("${external.karza.api.endpoint.extract.gst.url}")
    private String karzaExtractGSTDetailsEndpoint;

   // @Value("${external.karza.api.endpoint.verify.gst.url}")
    private String karzaVerifyGSTDetailsEndpoint;

    @Value(("${external.karza.api.endpoint.extract.pancard.url}"))
    private String karzaExtractPancardDetailsEndpoint;

    @Value("${external.karza.api.endpoint.verify.pancard.url}")
    private String karzaVerifyPancardDetailsEndpoint;

    @Value("${external.karza.api.endpoint.extract.aadhaar.url}")
    private String karzaExtractAadhaarDetailsEndpoint;

    @Value("${external.karza.api.endpoint.extract.bank.proof.url}")
    private String karzaExtractBankDetailsEndpoint;

    @Value("${external.karza.api.endpoint.verify.bank.proof.url}")
    private String karzaVerifyBankDetailsEndpoint;

    @Value("${external.karza.api.endpoint.cross.verify.pancard.gst.url}")
    private String karzaCrossVerifyPancardGSTEndpoint;

    @Value("${external.karza.api.endpoint.cross.verify.pancard.aadhaar.url}")
    private String karzaCrossVerifyPancardAadhaarEndpoint;

    @Value("${external.karza.api.endpoint.compare.name.url}")
    private String karzaNameCompareEndpoint;

    @Value("${external.karza.api.endpoint.extract.itr.url}")
    private String karzaExtractItrDetailsEndpoint;

    @Value("${allowed.compare.name.score}")
    private Integer allowedNameScore;

    //@Value("${kafka.doc.verification.seller.onboarding.event.topic}")
    private String kafkaDocVerificationSellerOnboardingEventTopic;

   // @Value("${kafka.doc.verification.karza.response.topic}")
    private String kafkaDocVerificationkarzaResponseTopic;

    @Value("${karza.async.response.retry.buffer.minutes:0}")
    private Long asyncResponseRetryBuffer;

    @Value("${karza.async.response.retry.limit:3}")
    private Integer asyncResponseRetryLimit;

    @Value("${karza.async.response.cron.rate.milli.seconds:300000}")
    private Long asyncResponseCronRate;

    @Value("${external.karza.api.endpoint.verify.aadhaar.url}")
    private String karzaVerifyAadhaarDetailsEndpoint;

    @Value("${karza.async.response.aadhaar.rely.on.pan.aadhaar:true}")
    private Boolean aadhaarVerificationRelyOnPan;



    //klub
    @Value("${app.external.klub.vendor-token}")
    private String klubVendorToken;

    @Value("${app.external.klub.vendor-code}")
    private String klubVendorCode;

    //common
    @Value("${app.external.common.vendor-token}")
    private String commonVendorToken;

    @Value("${app.external.common.vendor-code}")
    private String commonVendorCode;


    //flexi
    @Value("${app.external.flexi.vendor-token}")
    private String flexiVendorToken;

    @Value("${app.external.flexi.vendor-code}")
    private String flexiVendorCode;

    @Value("${app.external.flexi.redirection-url}")
    private String flexiRedirectUrl;


    //database

    @Value("${spring.datasource.primary.hikari.maximum-pool-size}")
    private int maxPoolSize;

    @Value("${spring.datasource.primary.hikari.minimum-idle}")
    private int minIdle;

    @Value("${spring.datasource.primary.hikari.pool-name}")
    private String poolName;

    @Value("${spring.datasource.primary.jdbc-url}")
    private String jdbcUrlPrimary;

    @Value("${spring.datasource.primary.username}")
    private String usernamePrimary;

    @Value("${spring.datasource.primary.password}")
    private String passwordPrimary;


    @Value("${spring.datasource.secondary.jdbc-url}")
    private String jdbcUrlSecondary;

    @Value("${spring.datasource.secondary.username}")
    private String usernameSecondary;

    @Value("${spring.datasource.secondary.password}")
    private String passwordSecondary;


    //kafka

    @Value("${kafka.topics.statusUpdate}")
    private String capitalTopicName;


    @Value("${app.web-url}")
    private String capitalWebUrl;

    @Value("${external.crif.access-code.user}")
    private String CrifUser;
    @Value("${external.crif.access-code.password}")
    private String CrifPassword;
    @Value("${external.crif.access-code.app-id}")
    private String CrifAppId;
    @Value("${external.crif.access-code.company-name}")
    private String CrifCompanyName;
    @Value("${external.crif.access-code.customer-id}")
    private String CrifCustomerId;

    @Value("${external.crif.access-code.product-code}")
    private String CrifProductCode;

    //CRIF EXTERNAL API

    @Value("${external.crif.base.uri}")
    private String CrifBaseUri;

    @Value("${external.crif.api.endpoint.extract.stage1.url}")
    private String CrifExtractStage1Endpoint;

    @Value("${external.crif.api.endpoint.extract.stage2.url}")
    private String CrifExtractStage2Endpoint;

    @Value("${aes.secret.key}")
    private String aesSecretKey;

    @Value("${is.encryption.enabled}")
    private String isEncryptionEnabled;

    @Value("${aes.iv.key}")
    private String aesIVKey;

    @Value("${jobs.crif.purge.expireAt}")
    private String crifConsentExpireAt;

    @Value("${external.crif.public.company-id}")
    private String publicCompanyId;
    @Value("${external.google.baseUri}")
    private String googleBaseUri;

    @Value("${external.google.captcha-secret-key}")
    private String captchaSiteVerificationSecretKey;

    @Value("${external.crif.public.user-id}")
    private String publicUserId;
    @Value("${external.google.endpoints.captcha-site-verify}")
    private String captchaSiteVerificationEndpoint;

    @Value("${app.captcha-verification.enabled}")
    private Boolean captchaVerificationEnabled;

    @Value("${app.rate-limit.enabled}")
    private Boolean rateLimitEnabled;

    @Value("${app.rate-limit.duration-in-minute}")
    private Long rateLimitDurationInMinute;

    @Value("${app.rate-limit.allowed-request}")
    private Long rateLimitAllowedRequest;

    @Value("${global.loan-vendor-id}")
    private Long globalVendorId;
}
