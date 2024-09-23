package com.sr.capital.kyc.external.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.kyc.external.request.extraction.data.ItrExtractionData;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ItrExtractionRequest extends KarzaBaseRequest<ItrExtractionData> {
}
