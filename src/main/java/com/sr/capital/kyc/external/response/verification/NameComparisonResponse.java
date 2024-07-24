package com.sr.capital.kyc.external.response.verification;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.verification.data.NameComparisonResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NameComparisonResponse extends KarzaBaseResponse<NameComparisonResponseData> {

}
