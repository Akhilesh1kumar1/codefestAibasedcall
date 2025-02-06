package com.sr.capital.external.truthscreen.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import lombok.*;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenPanToGstDecryptedResponseDto extends TruthScreenBaseResponse {

    private int status;
    @JsonProperty("ts_trans_id")
    private String tsTransId;
    private List<Map<String, Object>> msg;
}

