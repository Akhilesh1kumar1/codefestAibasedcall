package com.sr.capital.external.common.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DocumentUploadRequestDto {

    String fileName;

    File file;

    String documentType;

    String documentCategory;

    Map<String,String> metaData;

    String businessPartnerCode;

    String key;

    ByteArrayResource inputStream;

}
