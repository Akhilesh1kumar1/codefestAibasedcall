package com.sr.capital.external.corpveda.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CorpVedaPlaceOrderRequestExtractionData {

    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("cin")
    private String cin;
    @JsonProperty("is_documents_required")
    private int isDocumentsRequired;

}
