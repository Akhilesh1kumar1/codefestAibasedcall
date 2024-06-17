package com.sr.capital.entity.primary;

import com.sr.capital.config.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.COMPANY_LOAN_VENDOR;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = COMPANY_LOAN_VENDOR)
public class CompanyLoanVendorMappingEntity extends LongBaseEntity{


    @Column(name = "sr_company_id")
    Long srCompanyId;

    @Column(name = "partner_id")
    Long partnerId;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "client_id")
    String clientId;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "client_secret")
    String clientSecret;

    @Column(name = "app_id")
    String appId;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "app_secret")
    String appSecret;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "username")
    String userName;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "password")
    String password;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "auth_code")
    String authCode;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "refresh_token", length = 1024)
    String refreshToken;

    @Column(name = "description", length = 1024)
    String description;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "access_token", length = 1024)
    String accessToken;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "basic_auth")
    String basicAuth;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "kms_key")
    String kmsKey;

}
