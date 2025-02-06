package com.sr.capital.external.truthscreen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenGstinDecryptedResponseDto extends TruthScreenBaseResponse {

    private int status;
    private Map<String,Object> msg;

}
