package com.sr.capital.external.corpveda.dto.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CorpVedaLoginRequestData {

    private String username;
    private String password;
}
