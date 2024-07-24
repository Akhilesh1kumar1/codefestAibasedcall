package com.sr.capital.kyc.external.request.verification;

import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.verification.data.PanGstCrossVerificationData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PanGstCrossVerificationRequest extends KarzaBaseRequest<PanGstCrossVerificationData> {

}
