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
public class PanToGstDetails {

    private int status;
    @JsonProperty("ts_trans_id")
    private String tsTransId;
    private List<GSTDetails> msg;

    public static void encryptInfo(PanToGstDetails details, AES256 aes256){
        details.setMsg(GSTDetails.encryptInfo(details.getMsg(),aes256));
    }

    public static void decryptInfo(PanToGstDetails details, AES256 aes256){
        details.setMsg(GSTDetails.decryptInfo(details.getMsg(),aes256));
    }

}
