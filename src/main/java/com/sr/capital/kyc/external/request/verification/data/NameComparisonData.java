package com.sr.capital.kyc.external.request.verification.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class NameComparisonData implements Serializable {

    private String name1;

    private String name2;

}
