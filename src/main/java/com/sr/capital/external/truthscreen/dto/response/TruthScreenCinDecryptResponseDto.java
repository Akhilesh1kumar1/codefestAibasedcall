package com.sr.capital.external.truthscreen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenCinDecryptResponseDto extends TruthScreenBaseResponse {

    private String status;
    private List<Map<String,Object>> msg;
}
