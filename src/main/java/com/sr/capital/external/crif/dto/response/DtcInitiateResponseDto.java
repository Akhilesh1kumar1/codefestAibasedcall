package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtcInitiateResponseDto {

    String reportId;

    String orderId;

    String status;

    String redirectURL;
}
