package com.sr.capital.external.truthscreen.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omunify.encryption.algorithm.AES256;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PanDetails {


    private String lastUpdate;
    private String name;
    private String nameOnTheCard;
    private String panHolderStatusType;
    private String docNumber;
    private String status;
    private String statusDescription;
    private int sourceId;

    public static void encryptInfo(PanDetails config, AES256 aes256) {
        config.setDocNumber(aes256.encrypt(config.docNumber));
        config.setName(aes256.encrypt(config.getName()));
        config.setNameOnTheCard(aes256.encrypt(config.getNameOnTheCard()));
    }

    public static void decryptInfo(PanDetails config, AES256 aes256) {
        config.setDocNumber(aes256.decrypt(config.getDocNumber()));
        config.setName(aes256.decrypt(config.getName()));
        config.setNameOnTheCard(aes256.decrypt(config.getNameOnTheCard()));
    }


}
