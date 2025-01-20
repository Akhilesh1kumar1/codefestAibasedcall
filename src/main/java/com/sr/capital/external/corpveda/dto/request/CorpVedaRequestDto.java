package com.sr.capital.external.corpveda.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonNaming
public class CorpVedaRequestDto {

    private String cin;

}
