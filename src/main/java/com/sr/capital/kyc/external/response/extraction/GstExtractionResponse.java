package com.sr.capital.kyc.external.response.extraction;

import com.sr.capital.kyc.external.response.IdfyBaseResponse;
import com.sr.capital.kyc.external.response.extraction.data.GstExtractionResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GstExtractionResponse extends IdfyBaseResponse<GstExtractionResponseData> {

}
