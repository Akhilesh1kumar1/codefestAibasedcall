package com.sr.capital.util;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.*;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.TaskType;

import java.util.List;

public class MongoEntityUtil {

    public static Boolean validateKycDocDetails(KycDocDetails<?> kycDocDetails) {
        DocType docType = kycDocDetails.getDocType();
        switch (docType){
            case GST_BY_PAN:
                return kycDocDetails.getDetails() instanceof GstByPanDocDetails;
           /* case GST:
                return kycDocDetails.getDetails() instanceof GstDocDetails;
            case PAN:
                return kycDocDetails.getDetails() instanceof PanDocDetails;
            case AADHAAR:
                return kycDocDetails.getDetails() instanceof AadhaarDocDetails;*/
            case BANK_CHEQUE:
                List<BankDocDetails> bankDocDetailsList = (List<BankDocDetails>) kycDocDetails.getDetails();
                return bankDocDetailsList.get(bankDocDetailsList.size()-1) instanceof BankDocDetails;
            case ITR:
                return kycDocDetails.getDetails() instanceof ItrDocDetails;
            case DRIVING_LICENCE:
            case PROPRIETORSHIP:
            case VOTING_CARD:
            case CIN:
            case AGREEMENT:
            case MSME:
            case PROVISIONAL:
            case LOAN_TRACKER:
            case DIRECTORS:
            case PAN_GUARANTOR:
            case PAN_PERSONAL:
            case PASSPORT:
            case ADHAR_GUARANTOR_COAPPLICANT:
            case GST_REGISTRATION:
            case SHOP_EST_REGISTRATION:
            case TRADE_LICENSE:
            case FOOD_LICENSE:
            case DRUG_LICENSE_CERTIFICATE:
            case UDYAM_REGISTRATION:
            case UDYOG_AADHAAR:
            case BANK_STATEMENT_CURRENT_6:
            case ELECTRICITY_COMPANY:
            case SALE_DEED_COMPANY:
            case LANDLINE_BILL_3MONTH:
            case PROPERTY_TAX_RECEIPT:
            case RENT_AGREEMENT_COMPANY:
            case FINANCIAL_AUDIT:
            case ITR_RETURNS:
            case GST_RETURNS_6:
            case VALID_PARTNERSHIP_DEED:
            case COMPANY_PAN:
            case COMPANY_COI:
            case MOA_AOA_COMPANY:
            case LATEST_CA_SHAREHOLDINGS:
            case ELECTRICITY:
            case PIPED_GAS_BILL:
            case WATER_BILL:
            case SALE_DEED:
            case LANDLINE_BILL:
            case PAN:
            case AADHAR:
            case GST:
            case EPAN:
            case TRADE_CERTIFICATE:
                return kycDocDetails.getDetails() instanceof ReportMetaData;
            case PERSONAL_ADDRESS:
                return kycDocDetails.getDetails() instanceof PersonalAddressDetails;

            case BUSINESS_ADDRESS:
                return kycDocDetails.getDetails() instanceof BusinessAddressDetails;
            default:
                return false;
        }
    }

    public static Boolean validateKycVerifiedDetails(KycVerifiedDetails<?> kycVerifiedDetails) {
        TaskType taskType = kycVerifiedDetails.getTaskType();
        switch (taskType){
            case GST:
                return kycVerifiedDetails.getDetails() instanceof GstVerifiedDetails;
            case PAN:
                return kycVerifiedDetails.getDetails() instanceof PanVerifiedDetails;
            case AADHAAR:
                return kycVerifiedDetails.getDetails() instanceof AadhaarVerifiedDetails;
            case BANK_DETAILS:
                return kycVerifiedDetails.getDetails() instanceof BankVerifiedDetails;
            case PAN_GST:
                return kycVerifiedDetails.getDetails() instanceof PanGstCrossVerifiedDetails;
            case PAN_AADHAAR:
                return kycVerifiedDetails.getDetails() instanceof PanAadhaarCrossVerifiedDetails;
            default:
                return false;
        }
    }
}
