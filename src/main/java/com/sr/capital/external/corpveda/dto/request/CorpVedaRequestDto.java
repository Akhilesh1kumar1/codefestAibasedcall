package com.sr.capital.external.corpveda.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming
@Builder
public class CorpVedaRequestDto {

    private String docValue;
    private int docType;

}
