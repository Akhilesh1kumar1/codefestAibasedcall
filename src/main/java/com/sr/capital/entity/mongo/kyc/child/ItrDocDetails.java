package com.sr.capital.entity.mongo.kyc.child;

import com.sr.capital.kyc.external.constants.KarzaConstant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItrDocDetails {

    String username;

    String password;

    String apiVersion="1.0.2";

    @Builder.Default
    String consent= KarzaConstant.DEFAULT_CONSENT;
    @Builder.Default
    boolean additionalData=true;

    @Builder.Default
    Integer numberOfYears=1;

    String requestId;

    String financialYear;
}
