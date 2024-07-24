package com.sr.capital.kyc.external.request;

import com.sr.capital.kyc.external.request.extraction.data.DocumentExtractionData;
import com.sr.capital.kyc.external.request.extraction.data.GstExtractionData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GstExtractionRequest extends KarzaBaseRequest<GstExtractionData> {

}
