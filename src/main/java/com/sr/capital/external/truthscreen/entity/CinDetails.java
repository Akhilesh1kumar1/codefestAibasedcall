package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omunify.encryption.algorithm.AES256;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CinDetails {

    private String cin;
    private String companyName;

    public static void encryptInfo(CinDetails cinDetails, AES256 aes256){
        cinDetails.setCin(aes256.encrypt(cinDetails.getCin()));
        cinDetails.setCompanyName(aes256.encrypt(cinDetails.getCompanyName()));
    }

    public static void decryptInfo(CinDetails cinDetails, AES256 aes256) {
        cinDetails.setCin(aes256.decrypt(cinDetails.getCin()));
        cinDetails.setCompanyName(aes256.decrypt(cinDetails.getCompanyName()));
    }
}
