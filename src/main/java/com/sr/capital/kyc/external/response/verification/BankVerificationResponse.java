package com.sr.capital.kyc.external.response.verification;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.verification.data.BankVerificationResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankVerificationResponse extends KarzaBaseResponse<BankVerificationResponseData> {

}
