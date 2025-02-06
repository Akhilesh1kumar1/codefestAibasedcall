package com.sr.capital.external.truthscreen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenGstAnalyticsDecryptedResponseDto extends TruthScreenBaseResponse{

    private String transID;
    private Boolean isOtpValidated;
    private String tsTransId;
    private int status;
}
