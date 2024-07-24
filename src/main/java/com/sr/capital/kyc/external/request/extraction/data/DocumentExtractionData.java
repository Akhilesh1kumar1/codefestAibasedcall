package com.sr.capital.kyc.external.request.extraction.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentExtractionData implements Serializable {

    private String document1;


}
