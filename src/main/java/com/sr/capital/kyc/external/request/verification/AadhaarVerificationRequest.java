package com.sr.capital.kyc.external.request.verification;


import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.request.verification.data.AadhaarVerificationData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AadhaarVerificationRequest extends IdfyBaseRequest<AadhaarVerificationData> {

}
