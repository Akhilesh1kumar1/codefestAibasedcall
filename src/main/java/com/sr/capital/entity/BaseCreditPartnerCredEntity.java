package com.sr.capital.entity;

import com.sr.capital.config.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.BASE_CREDIT_PARTNER_META_DATA;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = BASE_CREDIT_PARTNER_META_DATA)
public class BaseCreditPartnerCredEntity extends LongBaseEntity{



    @Column(name = "partner_id")
    Long partnerId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "client_id")
    String clientId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "client_secret")
    String clientSecret;

    @Column(name = "app_id")
    String appId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "app_secret")
    String appSecret;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "username")
    String userName;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "password")
    String password;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "auth_code")
    String authCode;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "refresh_token", length = 1024)
    String refreshToken;

    @Column(name = "description", length = 1024)
    String description;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "access_token", length = 1024)
    String accessToken;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "basic_auth")
    String basicAuth;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "kms_key")
    String kmsKey;

}
