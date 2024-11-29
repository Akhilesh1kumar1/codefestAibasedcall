package com.sr.capital.external.flexi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RejectSanctionResponseDto {

    Boolean success;

    String message;
}
