package com.sr.capital.kyc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifierResponse {

    private Boolean isVerified;

    private String comments;
}
