package com.sr.capital.kyc.external.response.extraction;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.extraction.data.PanCardExtractionResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PanCardExtractionResponse extends KarzaBaseResponse<PanCardExtractionResponseData> {

}
