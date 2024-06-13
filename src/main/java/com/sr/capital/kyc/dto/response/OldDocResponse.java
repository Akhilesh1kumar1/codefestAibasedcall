package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OldDocResponse {
    @JsonProperty("doc_id")
    private String docId;
}
