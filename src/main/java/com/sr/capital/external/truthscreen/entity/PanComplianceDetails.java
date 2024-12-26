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
public class PanComplianceDetails {

    private boolean compliant;
    private String entityName;
    private String financialYear;
    private String isSpecified;
    private String panAadhaarLinkStatus;
    private String panAllotmentDateString;
    private String panNumber;
    private String panStatus;
    private String transId;
    private String tsTransId;

    public static void encrypt(PanComplianceDetails details, AES256 aes256){
        details.setEntityName(aes256.encrypt(details.getEntityName()));
        details.setFinancialYear(aes256.encrypt(details.getFinancialYear()));
        details.setPanAllotmentDateString(aes256.encrypt(details.getPanAllotmentDateString()));
        details.setPanNumber(aes256.encrypt(details.getPanNumber()));
    }

    public static void decrypt(PanComplianceDetails details, AES256 aes256){
        details.setEntityName(aes256.decrypt(details.getEntityName()));
        details.setFinancialYear(aes256.decrypt(details.getFinancialYear()));
        details.setPanAllotmentDateString(aes256.decrypt(details.getPanAllotmentDateString()));
        details.setPanNumber(aes256.decrypt(details.getPanNumber()));
    }

}
