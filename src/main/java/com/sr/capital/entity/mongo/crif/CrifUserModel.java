package com.sr.capital.entity.mongo.crif;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("crif-user-details")
public class CrifUserModel extends BaseDoc {

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("email")
    private String email;

    @Field("mobile")
    private String mobile;

    @Field("doc_type")
    private String documentType;

    @Field("doc_value")
    private String documentValue;

    @Field("is_otp_verified")
    private Boolean isOtpVerified;

    @Field("verification_token")
    private UUID verificationToken;



}