package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.sr.capital.kyc.dto.response.verified.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VerifiedDocData {

    private String tenantId;

    private BankVerifiedData bankVerifiedData;

    private GstVerifiedData gstVerifiedData;

    private PanAadhaarVerifiedData panAadhaarVerifiedData;

    private PanGstVerifiedData panGstVerifiedData;

    private PanVerifiedData panVerifiedData;

}
