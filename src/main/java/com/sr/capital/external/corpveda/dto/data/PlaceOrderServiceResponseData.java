package com.sr.capital.external.corpveda.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.corpveda.dto.response.CorpVedaBaseResponse;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PlaceOrderServiceResponseData extends CorpVedaBaseResponse {


    private Boolean status;
    private String code;
    private String type;
    private String message;
    private Map<String,Object> data;
    @JsonProperty("reference_id")
    private int referenceId;

}
