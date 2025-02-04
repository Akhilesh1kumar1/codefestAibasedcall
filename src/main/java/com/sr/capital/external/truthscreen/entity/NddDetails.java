package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.EncryptField;
import com.sr.capital.external.truthscreen.dto.response.InnerNDDDatabaseResponseData;
import com.sr.capital.external.truthscreen.dto.response.NDDDatabaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document("ndd_details")
public class NddDetails {

    private String transId;
    @JsonProperty("CREDIT_DEFAULT")
    private NDDDatabaseResponse creditDefault;
    @JsonProperty("Regulatory")
    private NDDDatabaseResponse regulatory;
    @JsonProperty("SANCTION")
    private NDDDatabaseResponse sanction;
    @JsonProperty("CRIMINAL")
    private NDDDatabaseResponse criminal;
    @JsonProperty("DEFAULTING_DIRECTORS_AND_COMPANIES")
    private NDDDatabaseResponse defaultDirectorsAndCompanies;
    private String status_code;
    private String message;
    private String tsTransId;

    public static void encrypt(NddDetails config,AES256 aes256){
        if (config.getCreditDefault() != null) {
            config.setCreditDefault(NDDDatabaseResponse.encrypt(config.getCreditDefault(), aes256));
        }

        if (config.getRegulatory() != null) {
            config.setRegulatory(NDDDatabaseResponse.encrypt(config.getRegulatory(), aes256));
        }

        if (config.getSanction() != null) {
            config.setSanction(NDDDatabaseResponse.encrypt(config.getSanction(), aes256));
        }

        if (config.getCriminal() != null) {
            config.setCriminal(NDDDatabaseResponse.encrypt(config.getCriminal(), aes256));
        }

        if (config.getDefaultDirectorsAndCompanies() != null) {
            config.setDefaultDirectorsAndCompanies(NDDDatabaseResponse.encrypt(config.getDefaultDirectorsAndCompanies(), aes256));
        }
    }

}
