package com.sr.capital.kyc.dto.request.child;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateBankDocDetailsRequest {

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("micr_code")
    private String micrCode;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonProperty("ifsc_code")
    private String ifscCode;
}
