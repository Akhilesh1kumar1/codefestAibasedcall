package com.sr.capital.external.truthscreen.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class TestDecryptResponseDto {
    private Map<String, Object> msg;
    private int status;


}
