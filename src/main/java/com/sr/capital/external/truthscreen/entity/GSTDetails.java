package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class GSTDetails {
    private String authStatus;
    private String gstin;
    private String stateCd;

    public static List<GSTDetails> encryptInfo(List<GSTDetails> detailsList, AES256 aes256) {
        if (detailsList != null) {
            detailsList.forEach(details -> details.setGstin(aes256.encrypt(details.getGstin())));
        }
        return detailsList;
    }

    public static List<GSTDetails> decryptInfo(List<GSTDetails> detailsList, AES256 aes256){
        if (detailsList != null) {
            detailsList.forEach(details -> details.setGstin(aes256.decrypt(details.getGstin())));
        }
        return detailsList;
    }
}
