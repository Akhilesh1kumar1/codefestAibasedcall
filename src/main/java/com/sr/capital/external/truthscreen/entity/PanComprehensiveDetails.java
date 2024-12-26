package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omunify.encryption.algorithm.AES256;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanComprehensiveDetails {
    private ExtractionData data;
    private String message;
    private String messageCode;
    private int status;
    private int statusCode;
    private boolean success;
    @JsonProperty("tsTransID")
    private String tsTransID;

    public static void encryptInfo(PanComprehensiveDetails details, AES256 aes256){
        ExtractionData.encryptData(details.getData(),aes256);
    }

    public static void decryptInfo(PanComprehensiveDetails details,AES256 aes256){
        ExtractionData.decryptData(details.getData(),aes256);
    }

}
