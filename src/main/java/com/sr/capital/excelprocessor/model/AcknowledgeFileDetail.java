package com.sr.capital.excelprocessor.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class AcknowledgeFileDetail {
    private String fileName;
    private String identifier;
}