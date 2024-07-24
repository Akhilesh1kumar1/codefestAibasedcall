package com.sr.capital.kyc.external.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GstDetailsByPanExtractionRequest extends KarzaBaseRequest<GstSearchByPanRequest>{
}
