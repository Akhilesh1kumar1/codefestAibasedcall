package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankDetailsResponse {

    @JsonProperty("success")
    private Boolean success;

}
