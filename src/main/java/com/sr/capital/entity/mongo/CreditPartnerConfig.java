package com.sr.capital.entity.mongo;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("credit_partner")
public class CreditPartnerConfig extends BaseDoc {

    @Indexed(unique = true, name = "creditPartnerId")
    Long partnerId;

    String accountId;

    String authCode;

    String refreshToken;

    String expiryDateFormat;

    @Builder.Default
    Boolean authCodeHardcoded = false;

    Map<String, String> metaData;

    @Builder.Default
    Long expiryMultiplier=1l;

    public static void encryptInfo(CreditPartnerConfig config, AES256 aes256) {
        config.setAccountId(aes256.encrypt(config.getAccountId()));
        config.setAuthCode(aes256.encrypt(config.getAuthCode()));
        config.setRefreshToken(aes256.encrypt(config.getRefreshToken()));
    }

    public static void decryptInfo(CreditPartnerConfig config, AES256 aes256) {
        config.setAccountId(aes256.decrypt(config.getAccountId()));
        config.setAuthCode(aes256.decrypt(config.getAuthCode()));
        config.setRefreshToken(aes256.decrypt(config.getRefreshToken()));
    }

}
