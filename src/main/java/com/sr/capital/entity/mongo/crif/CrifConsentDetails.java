package com.sr.capital.entity.mongo.crif;

import com.sr.capital.config.EncryptField;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "crif_consent_details")
@Getter
public class CrifConsentDetails extends BaseDoc {

    @Field("consent_id")
    private Long consentId;

    @Field("expired_at")
    private String expiredAt;

    @Field("expiration_method")
    private String expirationMethod;

    @Field("status")
    private String status;

    @Field("deleted_at")
    private String deletedAt;

    @Field("consent_date_history")
    private List<String> consentDateHistory;

}