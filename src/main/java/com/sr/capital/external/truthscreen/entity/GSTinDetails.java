package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omunify.encryption.algorithm.AES256;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document("gstin_details")
public class GSTinDetails {

    @Indexed(unique = true)
    private String transId;
    private String administrativeOffice;
    private String annualAggregateTurnover;
    private String constitutionOfBusiness;
    private String dateOfCancellation;
    private String dateOfRegistration;
    private String gstinUinStatus;
    private String gstinUin;
    private String grossTotalIncome;
    private String legalNameOfBusiness;
    private String natureOfBusinessActivities;
    private String natureOfCoreBusinessActivity;
    private String otherOffice;
    private String percentageOfTaxPaymentInCash;
    private String taxpayerType;
    private String tradeName;
    private String whetherAadhaarAuthenticated;
    private String whetherEKYCVerified;
    private String fieldVisitConducted;
    private Map<String, List<FilingData>> filingData;
    private Map<String, List<Service>> goodsAndService;
    private List<PlaceOfBusiness> placeOfBusinessData;
    private List<String> proprietorName;

    public static void encryptInfo(GSTinDetails config, AES256 aes256) {
        config.setAdministrativeOffice(aes256.encrypt(config.getAdministrativeOffice()));
        config.setAnnualAggregateTurnover(aes256.encrypt(config.getAnnualAggregateTurnover()));
        config.setConstitutionOfBusiness(aes256.encrypt(config.getConstitutionOfBusiness()));
        config.setDateOfCancellation(aes256.encrypt(config.getDateOfCancellation()));
        config.setDateOfRegistration(aes256.encrypt(config.getDateOfRegistration()));
        config.setGstinUinStatus(aes256.encrypt(config.getGstinUinStatus()));
        config.setGstinUin(aes256.encrypt(config.getGstinUin()));
        config.setGrossTotalIncome(aes256.encrypt(config.getGrossTotalIncome()));
        config.setLegalNameOfBusiness(aes256.encrypt(config.getLegalNameOfBusiness()));
        config.setNatureOfBusinessActivities(aes256.encrypt(config.getNatureOfBusinessActivities()));
        config.setNatureOfCoreBusinessActivity(aes256.encrypt(config.getNatureOfCoreBusinessActivity()));
        config.setOtherOffice(aes256.encrypt(config.getOtherOffice()));
        config.setPercentageOfTaxPaymentInCash(aes256.encrypt(config.getPercentageOfTaxPaymentInCash()));
        config.setTaxpayerType(aes256.encrypt(config.getTaxpayerType()));
        config.setTradeName(aes256.encrypt(config.getTradeName()));
        config.setWhetherAadhaarAuthenticated(aes256.encrypt(config.getWhetherAadhaarAuthenticated()));
        config.setWhetherEKYCVerified(aes256.encrypt(config.getWhetherEKYCVerified()));
        config.setFieldVisitConducted(aes256.encrypt(config.getFieldVisitConducted()));
    }

    public static void decryptInfo(GSTinDetails config, AES256 aes256) {
        config.setAdministrativeOffice(aes256.decrypt(config.getAdministrativeOffice()));
        config.setAnnualAggregateTurnover(aes256.decrypt(config.getAnnualAggregateTurnover()));
        config.setConstitutionOfBusiness(aes256.decrypt(config.getConstitutionOfBusiness()));
        config.setDateOfCancellation(aes256.decrypt(config.getDateOfCancellation()));
        config.setDateOfRegistration(aes256.decrypt(config.getDateOfRegistration()));
        config.setGstinUinStatus(aes256.decrypt(config.getGstinUinStatus()));
        config.setGstinUin(aes256.decrypt(config.getGstinUin()));
        config.setGrossTotalIncome(aes256.decrypt(config.getGrossTotalIncome()));
        config.setLegalNameOfBusiness(aes256.decrypt(config.getLegalNameOfBusiness()));
        config.setNatureOfBusinessActivities(aes256.decrypt(config.getNatureOfBusinessActivities()));
        config.setNatureOfCoreBusinessActivity(aes256.decrypt(config.getNatureOfCoreBusinessActivity()));
        config.setOtherOffice(aes256.decrypt(config.getOtherOffice()));
        config.setPercentageOfTaxPaymentInCash(aes256.decrypt(config.getPercentageOfTaxPaymentInCash()));
        config.setTaxpayerType(aes256.decrypt(config.getTaxpayerType()));
        config.setTradeName(aes256.decrypt(config.getTradeName()));
        config.setWhetherAadhaarAuthenticated(aes256.decrypt(config.getWhetherAadhaarAuthenticated()));
        config.setWhetherEKYCVerified(aes256.decrypt(config.getWhetherEKYCVerified()));
        config.setFieldVisitConducted(aes256.decrypt(config.getFieldVisitConducted()));
    }
}
