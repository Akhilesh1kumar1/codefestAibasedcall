package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.kyc.external.constants.KarzaConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItrDocDetails {

    String username;

    String password;

    @Builder.Default
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
