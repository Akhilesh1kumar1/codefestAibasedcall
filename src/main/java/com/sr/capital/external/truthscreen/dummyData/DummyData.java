package com.sr.capital.external.truthscreen.dummyData;

import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DummyData<T> extends TruthScreenBaseResponse<T> {

    private int status;
    private Map<String, Object> msgs;


}
