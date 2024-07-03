package com.sr.capital.kyc.external.request;

import com.sr.capital.kyc.dto.request.VerifyGstOtpRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class VerifyGstExtractionRequest extends KarzaBaseRequest<VerifyGstOtpRequest>{
}
