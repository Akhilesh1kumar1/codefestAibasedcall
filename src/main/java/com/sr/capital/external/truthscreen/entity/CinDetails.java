package com.sr.capital.external.truthscreen.entity;

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
public class CinDetails {

    private int status;
    private List<CinInnerDetails> msg;

    public static void encryptInfo(CinDetails details, AES256 aes256){
        details.setMsg(CinInnerDetails.encryptInfo(details.getMsg(),aes256));
    }

    public static void decryptInfo(CinDetails details, AES256 aes256){
        details.setMsg(CinInnerDetails.decryptInfo(details.getMsg(),aes256));
    }
}
