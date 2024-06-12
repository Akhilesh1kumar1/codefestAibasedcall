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


}
