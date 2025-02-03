package com.sr.capital.excelprocessor.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ProcessUploadDataMessage {
    private String fileName;
    private String userId;
    private String correlationId;
}