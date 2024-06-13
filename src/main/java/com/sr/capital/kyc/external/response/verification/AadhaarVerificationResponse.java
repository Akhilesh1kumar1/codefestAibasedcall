package com.sr.capital.kyc.external.response.verification;

import com.sr.capital.kyc.external.response.IdfyBaseResponse;
import com.sr.capital.kyc.external.response.verification.data.AadhaarVerificationResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AadhaarVerificationResponse extends IdfyBaseResponse<AadhaarVerificationResponseData> {

}
