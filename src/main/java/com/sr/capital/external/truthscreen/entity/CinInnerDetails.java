package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CinInnerDetails {

    private String cin;
    @JsonProperty("company_name")
    private String companyName;

    public static List<CinInnerDetails> encryptInfo(List<CinInnerDetails> detailsList, AES256 aes256) {
        if (detailsList != null) {
            detailsList.forEach(details -> details.setCin(aes256.encrypt(details.getCin())));
        }
        return detailsList;
    }

    public static List<CinInnerDetails> decryptInfo(List<CinInnerDetails> detailsList, AES256 aes256){
        if (detailsList != null) {
            detailsList.forEach(details -> details.setCin(aes256.decrypt(details.getCin())));
        }
        return detailsList;
    }

}
