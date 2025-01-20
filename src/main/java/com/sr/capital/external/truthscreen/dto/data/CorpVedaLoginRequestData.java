package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CorpVedaLoginRequestData {

    private String username;
    private String password;
}
