package com.sr.capital.external.corpveda.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorpVedaLoginResponseData {

    private String message;
    private String user;
    private String token;
    private String status;
}
