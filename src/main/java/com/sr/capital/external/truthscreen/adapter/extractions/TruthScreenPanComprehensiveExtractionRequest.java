package com.sr.capital.external.truthscreen.adapter.extractions;

import com.sr.capital.external.truthscreen.dto.data.PanComprehensiveExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TruthScreenPanComprehensiveExtractionRequest extends TruthScreenBaseRequest<PanComprehensiveExtractionRequestData> {

}
