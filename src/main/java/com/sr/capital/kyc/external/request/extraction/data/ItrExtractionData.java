package com.sr.capital.kyc.external.request.extraction.data;

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
public class ItrExtractionData {

    String username;

    String password;

    @Builder.Default
    String consent= KarzaConstant.DEFAULT_CONSENT;

    @Builder.Default
    Integer numberOfYears =2;

    @Builder.Default
    String apiVersion ="1.0.2";

    @Builder.Default
   Boolean additionalData=true;

}
