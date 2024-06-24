package com.sr.capital.kyc.external.response.verification;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.verification.data.GstVerificationResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GstVerificationResponse extends KarzaBaseResponse<GstVerificationResponseData> {

}
