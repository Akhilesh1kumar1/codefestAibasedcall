package com.sr.capital.external.corpveda.docs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.dto.response.CorpVedaBaseResponse;
import com.sr.capital.external.corpveda.dto.response.CorpVedaResponseDto;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;


@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class CorpVedaDocOrChestratorRequest<T> {

    private TruthScreenDocType docType;
    private String docNumber;
    @JsonProperty("sr_company_id")
    private String srCompanyId;
    @JsonProperty("reference_id")
    private String referenceId;
    private CorpVedaBaseRequest<?> corpVedaBaseRequest;
    private CorpVedaBaseResponse<?> cOrpVedaBaseResponse;
    private CorpVedaDocDetails<T> corpVedaDocDetails;


}
