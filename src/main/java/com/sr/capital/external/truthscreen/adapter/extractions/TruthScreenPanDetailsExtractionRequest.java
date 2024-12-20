package com.sr.capital.external.truthscreen.adapter.extractions;

import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;
//adadad

@Data
@EqualsAndHashCode(callSuper = true)
public class TruthScreenPanDetailsExtractionRequest extends TruthScreenBaseRequest<PanExtractionRequestData> {
}
