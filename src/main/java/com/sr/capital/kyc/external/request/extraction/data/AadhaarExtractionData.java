package com.sr.capital.kyc.external.request.extraction.data;

import com.sr.capital.kyc.external.constants.KarzaConstant;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AadhaarExtractionData implements Serializable {

    private String document1;

    private String document2;

    @Builder.Default
    private String consent = KarzaConstant.DEFAULT_CONSENT;

}
