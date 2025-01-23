package com.sr.capital.external.corpveda.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CorpVedaGetDataRequestDto {

    @JsonProperty("reference_id")
    private int referenceId;
}
