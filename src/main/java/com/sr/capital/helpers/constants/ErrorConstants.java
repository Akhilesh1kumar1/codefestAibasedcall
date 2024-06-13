package com.sr.capital.helpers.constants;

import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.kyc.dto.request.ErrorMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class ErrorConstants {

    public static final HttpStatus DEFAULT_CODE = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final String DEFAULT_MESSAGE = "Something went wrong! Please try again later.";

    public static final String DELIMITER = " :: ";
    public static final String FETCH_DOC_DETAILS_ERROR = "Invalid tenant id list !!";

    public static final String KYC_INVALID_GST_MESSAGE = "KYC is pending because of failure in validation of GST document.";
    public static final String KYC_INVALID_PAN_MESSAGE = "KYC is pending because of failure in validation of PAN document.";
    public static final String KYC_INVALID_AADHAAR_MESSAGE = "KYC is pending because of failure in validation of Aadhar document.";
    public static final String KYC_INVALID_BANK_MESSAGE = "KYC is pending because of failure in validation of Bank Account Details.";
    public static final String KYC_CROSS_VERIFICATION_ERROR_MESSAGE = "KYC is pending because of failure in cross verification of documents.";
    public static final String BANK_INVALID_ACCOUNT_MESSAGE = " invalid bank account number.";
    public static final String BANK_INVALID_IFSC_MESSAGE = " invalid IFSC code.";
    public static final String BANK_INVALID_PAN_BANK_MESSAGE = " a mismatch between your banking name and the name on your PAN card.";
    public static final String DOC_UPLOAD_ERROR_MESSAGE = "Sorry, we are unable to fetch your details at the moment, Kindly upload a valid/clear  document.";
    public static final String SIZE_LIMIT_ERROR_MESSAGE = "Oops! The size limit for images and documents is 2.0 MB, please reduce the file size and upload again.";


    public static String getErrorMessage(ErrorMessageRequest errorMessageRequest) {

        VerificationType verificationType = errorMessageRequest.getVerificationType();
        TaskType taskType = errorMessageRequest.getTaskType();
        if (VerificationType.DOC_VERIFICATION.equals(verificationType)){
            switch (taskType) {
                case AADHAAR:
                    return KYC_INVALID_AADHAAR_MESSAGE;
                case BANK_DETAILS:
                    return KYC_INVALID_BANK_MESSAGE;
                case GST:
                    return ErrorConstants.KYC_INVALID_GST_MESSAGE;
                case PAN:
                    return KYC_INVALID_PAN_MESSAGE;
                case PAN_AADHAAR:
                case PAN_BANK_DETAILS:
                case PAN_GST:
                    return KYC_CROSS_VERIFICATION_ERROR_MESSAGE;
                default:
                    return "";
            }
        } else {
            switch (taskType) {
                case BANK_DETAILS:
                    if(StringUtils.hasLength(errorMessageRequest.getComments()) && "INVALID BANK ACCOUNT".equalsIgnoreCase(errorMessageRequest.getComments().trim())) {
                        return BANK_INVALID_ACCOUNT_MESSAGE;
                    } else if (StringUtils.hasLength(errorMessageRequest.getComments()) && "Invalid IFSC provided".equalsIgnoreCase(errorMessageRequest.getComments().trim())) {
                        return BANK_INVALID_IFSC_MESSAGE;
                    }
                    return BANK_INVALID_ACCOUNT_MESSAGE;

                case PAN_BANK_DETAILS:
                    return BANK_INVALID_PAN_BANK_MESSAGE;
                default:
                    return "";
            }
        }

    }

}
