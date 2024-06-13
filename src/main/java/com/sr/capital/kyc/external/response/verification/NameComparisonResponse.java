package com.sr.capital.kyc.external.response.verification;

import com.sr.capital.kyc.external.response.IdfyBaseResponse;
import com.sr.capital.kyc.external.response.verification.data.NameComparisonResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NameComparisonResponse extends IdfyBaseResponse<NameComparisonResponseData> {

}
