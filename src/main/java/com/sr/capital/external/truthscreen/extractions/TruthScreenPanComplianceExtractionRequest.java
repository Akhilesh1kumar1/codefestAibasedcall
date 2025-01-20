package com.sr.capital.external.truthscreen.extractions;

import com.sr.capital.external.truthscreen.dto.data.PanComplianceExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TruthScreenPanComplianceExtractionRequest extends TruthScreenBaseRequest<PanComplianceExtractionRequestData> {
}
