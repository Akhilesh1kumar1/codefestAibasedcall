package com.sr.capital.kyc.external.request.extraction.data;

import com.sr.capital.kyc.external.constants.IdfyConstant;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AadhaarExtractionData implements Serializable {

    private String document1;

    private String document2;

    @Builder.Default
    private String consent = IdfyConstant.DEFAULT_CONSENT;

}
