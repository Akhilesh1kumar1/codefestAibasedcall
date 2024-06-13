package com.sr.capital.kyc.external.request.extraction.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DocumentExtractionData implements Serializable {

    private String document1;

}
