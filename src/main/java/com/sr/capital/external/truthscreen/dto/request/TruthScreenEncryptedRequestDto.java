package com.sr.capital.external.truthscreen.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TruthScreenEncryptedRequestDto {

    private String requestData;
}
