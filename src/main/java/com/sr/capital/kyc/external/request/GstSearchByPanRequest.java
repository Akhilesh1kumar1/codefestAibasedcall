package com.sr.capital.kyc.external.request;

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
public class GstSearchByPanRequest {

    @Builder.Default
    String consent = KarzaConstant.DEFAULT_CONSENT;

    String pan;
}
