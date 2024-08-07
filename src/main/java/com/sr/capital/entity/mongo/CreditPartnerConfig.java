package com.sr.capital.entity.mongo;

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

    @Indexed(unique = true,name = "creditPartnerId")
    Long partnerId;

    String accountId;

    String authCode;

    String refreshToken;

    Map<String,String> metaData;

}
