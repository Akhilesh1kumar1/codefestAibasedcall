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
            case GST:
                return kycDocDetails.getDetails() instanceof GstDocDetails;
            case PAN:
                return kycDocDetails.getDetails() instanceof PanDocDetails;
            case AADHAAR:
                return kycDocDetails.getDetails() instanceof AadhaarDocDetails;
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
