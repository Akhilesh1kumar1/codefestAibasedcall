package com.sr.capital.helpers.constants;

import java.util.Map;

import static com.sr.capital.helpers.constants.Constants.FileContentTypes.*;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;

public final class Constants {

    public static class EntityNames{
        public static final String BASE_CREDIT_PARTNER_TABLE_NAME ="base_credit_partners";
        public static final String BANK ="bank";
        public static final String ACCOUNT_TYPE ="account_type";

    }

    public static class ServiceConstants{
        public static final String UTILITY_INSTANTIATION_ERROR = "This class should not be instantiated as it is a utility class.";
        public static final String REQUEST_FAILED = "Given request failed.";


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
    public static final Map<String, String> FILE_CONTENT_TYPE_MAP = Map.of(CSV_EXTENSION, CSV_CONTENT_TYPE, XLS_EXTENSION, XLS_CONTENT_TYPE,
            XLSX_EXTENSION, XLSX_CONTENT_TYPE);
}
