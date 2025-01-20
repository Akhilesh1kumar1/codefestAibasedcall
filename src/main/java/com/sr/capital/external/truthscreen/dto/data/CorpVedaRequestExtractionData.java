package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CorpVedaRequestExtractionData {

    private String clientId;
    private String cin;
    @JsonProperty("is_documents_required")
    private int isDocumentsRequired;

}
