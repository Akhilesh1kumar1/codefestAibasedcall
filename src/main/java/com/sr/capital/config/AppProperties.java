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
        IDfy Credential
     */

    @Value("${external.idfy.account.id}")
    private String idfyAccountId;

    @Value("${external.idfy.api.key}")
    private String idfyApiKey;

    /*
        IDfy EXTERNAL API
     */

    @Value("${external.idfy.base.uri}")
    private String idfyBaseUri;

    @Value("${external.idfy.async.api.endpoint.tasks}")
    private String idfyAsyncTaskEndpoint;

    @Value("${external.idfy.api.endpoint.extract.gst.url}")
    private String idfyExtractGSTDetailsEndpoint;

    @Value("${external.idfy.api.endpoint.verify.gst.url}")
    private String idfyVerifyGSTDetailsEndpoint;

    @Value(("${external.idfy.api.endpoint.extract.pancard.url}"))
    private String idfyExtractPancardDetailsEndpoint;

    @Value("${external.idfy.api.endpoint.verify.pancard.url}")
    private String idfyVerifyPancardDetailsEndpoint;

    @Value("${external.idfy.api.endpoint.extract.aadhaar.url}")
    private String idfyExtractAadhaarDetailsEndpoint;

    @Value("${external.idfy.api.endpoint.extract.bank.proof.url}")
    private String idfyExtractBankDetailsEndpoint;

    @Value("${external.idfy.api.endpoint.verify.bank.proof.url}")
    private String idfyVerifyBankDetailsEndpoint;

    @Value("${external.idfy.api.endpoint.cross.verify.pancard.gst.url}")
    private String idfyCrossVerifyPancardGSTEndpoint;

    @Value("${external.idfy.api.endpoint.cross.verify.pancard.aadhaar.url}")
    private String idfyCrossVerifyPancardAadhaarEndpoint;

    @Value("${external.idfy.api.endpoint.compare.name.url}")
    private String idfyNameCompareEndpoint;

    @Value("${allowed.compare.name.score}")
    private Integer allowedNameScore;

    //@Value("${kafka.doc.verification.seller.onboarding.event.topic}")
    private String kafkaDocVerificationSellerOnboardingEventTopic;

   // @Value("${kafka.doc.verification.idfy.response.topic}")
    private String kafkaDocVerificationIdfyResponseTopic;

    @Value("${idfy.async.response.retry.buffer.minutes:0}")
    private Long asyncResponseRetryBuffer;

    @Value("${idfy.async.response.retry.limit:3}")
    private Integer asyncResponseRetryLimit;

    @Value("${idfy.async.response.cron.rate.milli.seconds:300000}")
    private Long asyncResponseCronRate;

    @Value("${external.idfy.api.endpoint.verify.aadhaar.url}")
    private String idfyVerifyAadhaarDetailsEndpoint;

    @Value("${idfy.async.response.aadhaar.rely.on.pan.aadhaar:true}")
    private Boolean aadhaarVerificationRelyOnPan;



    //klub
    @Value("${app.external.klub.vendor-token}")
    private String klubVendorToken;

    @Value("${app.external.klub.vendor-code}")
    private String klubVendorCode;
}
