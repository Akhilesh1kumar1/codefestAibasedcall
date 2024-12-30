package com.sr.capital.entity.mongo.crif;

import com.sr.capital.config.EncryptField;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("crif_user_details")
public class CrifUserModel extends BaseDoc {

    @EncryptField
    @Field("first_name")
    private String firstName;

    @EncryptField
    @Field("last_name")
    private String lastName;

    @EncryptField
    @Field("email")
    private String email;

    @Field("mobile")
    @EncryptField
    private String mobile;

    @Field("doc_type")
    private String documentType;

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("consent_date")
    private String consentDate;

    @EncryptField
    @Field("doc_value")
    private String documentValue;

    @Field("is_otp_verified")
    private Boolean isOtpVerified;

    @Field("verification_token")
    private UUID verificationToken;

    @Column(name = "utm_source")
    String utmSource;

    @Column(name = "utm_medium")
    String utmMedium;

    @Column(name = "utm_campaign")
    String utmCampaign;

    @Column(name = "utm_term")
    String utmTerm;

    @Column(name = "utm_content")
    String utmContent;

}