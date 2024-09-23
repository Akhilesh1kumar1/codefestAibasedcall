package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtractedBankResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("micr_code")
    private String micrCode;

    @JsonProperty("micr_cheque_number")
    private String micrChequeNumber;

    @JsonProperty("date_of_issue")
    private String dateOfIssue;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonProperty("ifsc_code")
    private String ifscCode;

    @JsonProperty("bank_address")
    private String bankAddress;

    private String bankAccountType;


}
