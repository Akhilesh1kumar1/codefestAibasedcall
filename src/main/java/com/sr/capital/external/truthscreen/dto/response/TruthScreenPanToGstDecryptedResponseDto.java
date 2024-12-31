package com.sr.capital.external.truthscreen.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenPanToGstDecryptedResponseDto extends TruthScreenBaseResponse {

    private String status;
    @JsonProperty("ts_trans_id")
    private String tsTransId;
    private List<Map<String, Object>> msg;
}

