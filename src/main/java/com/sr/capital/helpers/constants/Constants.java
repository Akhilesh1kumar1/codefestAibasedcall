package com.sr.capital.helpers.constants;

import com.sr.capital.helpers.enums.DocType;

import java.util.Map;
import java.util.Set;

import static com.sr.capital.helpers.constants.Constants.FileContentTypes.*;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;

public final class Constants {

    public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String MOBILE_REGEX = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";


    public static class EntityNames{
        public static final String BASE_CREDIT_PARTNER_TABLE_NAME ="base_credit_partners";
        public static final String BANK ="bank";
        public static final String ACCOUNT_TYPE ="account_type";

        public static final String TENANT_BANK_DETAILS = "tenant_bank_details";

        public static final String COMPANY_DETAILS = "company_kyc_details";

        public static final String USER = "user";

        public static final String ADHAR_DETAILS = "adhar_details";

        public static final String PAN_DETAILS = "pan_details";

        public static final String ENACH_LINKING = "enach_linking";

        public static final String DIRECTOR_KYC = "director_kyc";

        public static final String USER_MAPPING = "user_mapping";

        public static final String PROVIDER_CONFIG_TEMPLATE = "provider_template";

        public static final String PROVIDER_URL_CONFIG= "provider_url_config";

        public static final String COMPANY_LOAN_VENDOR = "company_loan_vendor";

        public static final String LOAN_OFFERS = "loan_offer";

        public static final String LOAN_APPLICATION = "loan_application";

        public static final String LOAN_APPLICATION_STATUS = "loan_application_status";

        public static final String LOAN_DISBURSED = "loan_disbursed";

        public static final String TASK = "task";

        public static final String CAPITAL_REPORT = "report_capital_data_report";

        public static final String COMPANY_WISE_REPORT = "reports_company_wise_report";
    }

    public static class ServiceConstants{
        public static final String UTILITY_INSTANTIATION_ERROR = "This class should not be instantiated as it is a utility class.";
        public static final String REQUEST_FAILED = "Given request failed.";
        public static final String FILE_FAILURE_REPORT_CSV_HEADER = "Error message\n";
        public static final String ROW_FAILURE_REPORT_CSV_HEADER = " ACTION, REASON, Error message\n";
        public static final String ERROR_FILE_NAME = "Error_%s%s%s";
        public static final String FILE_UPLOAD_RECORD = "FILE_UPLOAD_RECORD";
        public static final String PROGRESS_PERCENTAGE = "PROGRESS_PERCENTAGE";

        public static final String X_SR_TOKEN = "X-SR-Token";
//    public static final String X_CSRF_TOKEN_KEY = "X-CSRF-Token";

        public static final String X_GlAu_SECRET_KEY = "X-GlAu-Secret";
        public static final String X_SERVICE_SECRET = "X-Service-Secret";
        public static final String INVALID_FILE_DIRECTORY = "Invalid file directory : %s";

       public static final String BANK_IMAGE_FOLDER_NAME = "bank";
       public static final String ADHAR_IMAGE_FOLDER_NAME = "adhar";
       public static final String PAN_IMAGE_FOLDER_NAME = "pan";

    }

    public static class MessageConstants{
        public static final String ERROR_WHILE_READING_DATA_FROM_AWS = "Error while reading data : %s from AWS, error : %s";
        public static final String ERROR_WHILE_UPLOADING_FILE_TO_AWS = "Error while uploading file : %s to AWS, error : %s";
        public static final String PRESIGNED_URL_GENERATION = "Pre signed URL generated successfully.";
        public static final String CREDIT_PARTNER_CREATED_SUCCESSFULLY = "Credit partner added successfully";
        public static final String CREDIT_PARTNER_UPDATED_SUCCESSFULLY = "Credit partner updated successfully";
        public static final String NO_RECORD_FOUND_WITH_GIVEN_DETAILS = "Invalid Request, No record found for given details";

        public static final String BASE_BANK_CREATED_SUCCESSFULLY = "Base bank added successfully";
        public static final String BASE_ACCOUNT_TYPE_CREATED_SUCCESSFULLY = "Base account type added successfully";

        public static final String UNSUPPORTED_FILE_TYPE = "File Type: %s is not supported.";
        public static final String EMPTY_FILE_TYPE = "File Type cannot be empty.";

        public static final String INVALID_CORRELATION_ID = "No record exist for the given correlation ID.";
        public static final String EMPTY_CORRELATION_ID = "Correlation ID is empty in the payload.";
        public static final String FILE_NAME_MISMATCH = "Acknowledgement is being done for a different file concerned with correlation ID.";
        public static final String STATUS_MISMATCH = "The request is already acknowledged.";
        public static final String FILE_IN_PROGRESS_ERROR = "A file is already in progress. Please retry after some time.";

        public static final String VALID_PAN = "Pan Number is in valid format";

        public static final String INVALID_EMAIL = "The email address is invalid!";
        public static final String INVALID_MOBILE = "The mobile is invalid!";
        public static final String INVALID_USERNAME = "The username is invalid!";

        public static final String INVALID_PAN = "The pan is invalid!";

        public static final String INVALID_GST = "The gst is invalid!";

        public static final String EXPIRED_VERIFICATION_LINK = "The verification link has expired! Please request a new one.";
        public static final String INVALID_VERIFICATION_LINK = "The verification link is invalid! Please request a new one.";
        public static final String EXPIRED_OTP = "The OTP has expired! Please request a new one.";
        public static final String INCORRECT_OTP = "Incorrect OTP!";
        public static final String UNSUCCESSFUL_OTP_ATTEMPT = "Too many unsuccessful attempts! Please resend OTP or try again.";
        public static final String OTP_REQUEST_LIMIT = "You've reached the maximum limit of OTP requests!";
        public static final String INVALID_VERIFICATION_TOKEN = "[INVALID REQUEST] :: Invalid Token!";

        public static final String DECRYPTION_ERROR = "[INVALID REQUEST] :: Encryption Error! Unable to decrypt data.";

        public static final String REQUEST_FAILED = "Given request failed.";

        public static final String REQUEST_SUCCESS= "Given request successes.";

        public static final String ADHAR_NOT_FOUND = "Adhar Details not found";

        public static final String PAN_NOT_FOUND = "Pan details not found";

        public static final String PAN_SAVE_SUCCESS = "PAN details saved successfully";

        public static final String COMPANY_KYC_SAVED_SUCCESSFULLY = "Company kyc saved successfully";
        
    }

    public static final class Separators {
        private Separators() {
            throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
        }

        public static final String SLASH_SEPARATOR = "/";
        public static final String QUERY_PARAM_SEPARATOR = "?";
        public static final String UNDERSCORE_SEPARATOR = "_";
        public static final String EXTENSION_SEPARATOR = ".";
    }

    public static final class FileContentTypes {

        private FileContentTypes() {
            throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
        }

        public static final String CSV_EXTENSION = "csv";
        public static final String CSV_CONTENT_TYPE = "text/csv";
        public static final String XLS_EXTENSION = "xls";
        public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
        public static final String XLSX_EXTENSION = "xlsx";
        public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    public static class RedisKeys{
        public static final String GLOBAL_KEY = "capital";
        public static final String BASE_CREDIT_PARTNER =  "credit_partner";

        public static final String BANK_DETAILS = "banks";

        public static final String ACCOUNT_TYPE =  "account_type";

    }

    public static final class Headers {
        private Headers() {
            throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
        }

        public static final String CORRELATION_HEADER = "X-Correlation-ID";
        public static final String TENANT_HEADER = "X-Tenant-ID";
        public static final String USER_HEADER = "X-User-ID";
        public static final String REFERER_HEADER = "X-Referer";
        public static final String SERVICE_SECRET_HEADER = "X-Service-Secret";

        public static final String TOKEN = "X-Token";
    }


   public static class KaleyraHeaders{
        public static final String API_KEY="api-key";
        public static final String CONTENT_TYPE="Content-Type";
    }
   public static class ContentType{
        public static final String MULTIPART_FORM_DATA="multipart/form-data";
        public static final String APPLICATION_JSON="application/json";
    }
    public static final Map<String, String> FILE_CONTENT_TYPE_MAP = Map.of(CSV_EXTENSION, CSV_CONTENT_TYPE, XLS_EXTENSION, XLS_CONTENT_TYPE,
            XLSX_EXTENSION, XLSX_CONTENT_TYPE);

    public static final Set<String> REQUIRED_DOCUMENTS = Set.of(DocType.MSME.name(), DocType.GST.name(), DocType.BANK_CHEQUE.name(), DocType.PROPRIETORSHIP.name(),
            DocType.GST_BASIC.name(),DocType.GST_BY_PAN.name());
}
