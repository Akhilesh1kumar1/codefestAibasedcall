package com.sr.capital.external.truthscreen.adapter;

import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TruthScreenExternalRequestMetaData {

    private String endpoint;
    private TruthScreenDocType docType;
    private Class<?> responseClass;
}
