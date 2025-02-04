package com.sr.capital.external.truthscreen.dto.response;

import com.omunify.encryption.algorithm.AES256;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NDDDatabaseResponse {

    private InnerNDDDatabaseResponseData data;
    private String message;
    private String status_code;

    public static NDDDatabaseResponse encrypt(NDDDatabaseResponse config, AES256 aes256){
        config.setData(InnerNDDDatabaseResponseData.encrypt(config.getData(),aes256));
        return config;
    }


}
