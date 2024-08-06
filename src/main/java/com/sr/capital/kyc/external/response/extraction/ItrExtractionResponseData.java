package com.sr.capital.kyc.external.response.extraction;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.extraction.data.ItrAdditionalResponseData;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class ItrExtractionResponseData extends KarzaBaseResponse<ItrAdditionalResponseData>{
}
