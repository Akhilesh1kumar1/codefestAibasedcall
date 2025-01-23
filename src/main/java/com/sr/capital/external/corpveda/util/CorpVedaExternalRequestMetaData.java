package com.sr.capital.external.corpveda.util;

import com.sr.capital.external.corpveda.enums.CorpVedaDocType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CorpVedaExternalRequestMetaData {

    private String endpoint;
    private CorpVedaDocType docType;
    private Class<?> responseClass;
}
