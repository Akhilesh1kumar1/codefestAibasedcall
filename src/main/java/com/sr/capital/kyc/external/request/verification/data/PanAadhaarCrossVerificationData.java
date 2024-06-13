package com.sr.capital.kyc.external.request.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanAadhaarCrossVerificationData implements Serializable {

    private String panNumber;

    private String aadhaarNumber;

}
