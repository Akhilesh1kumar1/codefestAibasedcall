package com.sr.capital.kyc.external.request;

import com.sr.capital.helpers.enums.DocType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalRequestMetaData {

    private String endpoint;

    private DocType docType;

    private Class<?> responseClass;
}
