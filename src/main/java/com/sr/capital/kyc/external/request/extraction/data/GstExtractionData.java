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
public class GstExtractionData {

    String username;

    String gstin;

    String refId;

    @Builder.Default
    String consent= KarzaConstant.DEFAULT_CONSENT;
    @Builder.Default
    boolean consolidate=false;

    @Builder.Default
    boolean extendedPeriod=false;
}
