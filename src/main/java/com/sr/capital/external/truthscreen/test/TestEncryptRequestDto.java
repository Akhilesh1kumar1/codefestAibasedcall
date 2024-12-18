package com.sr.capital.external.truthscreen.test;

import lombok.Data;

@Data
public class TestEncryptRequestDto {
    private String transID;
    private int docType;
    private String docNumber;

    private int isAsync;
}
