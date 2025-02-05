package com.sr.capital.external.truthscreen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenBankRequestData {

    private String transID;
    private int docType;

}
