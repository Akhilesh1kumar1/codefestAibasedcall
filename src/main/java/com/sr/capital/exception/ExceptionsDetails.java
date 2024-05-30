package com.sr.capital.exception;

public class ExceptionsDetails {

    public static final String DEFAULT_MESSAGE = "Something went wrong! Unable to process the request at this moment. Please try again later.";
    public static final String INVALID_REQUEST = "Invalid Request!";


    public static class INTERNAL_ERROR {
        public static String CODE = "AUT251";
        public static String TITLE = "Internal Server Error";
        public static String MESSAGE = "Something went wrong! Please try again later.";
    }

    public static class CUSTOM_EXCEPTION {
        public static String CODE = "AUT252";
        public static String TITLE = "Custom Exception";
        public static String MESSAGE = "";
    }

    public static class CONSTRAINTS_VIOLATION {
        public static String CODE = "AUT253";
        public static String TITLE = "Constraints Violated";
        public static String MESSAGE = "[INVALID REQUEST] :: Constraints violated for the request!";
    }

    public static class INVALID_METHOD_ARGUMENTS {
        public static String CODE = "AUT254";
        public static String TITLE = "Invalid Method Arguments";
        public static String MESSAGE = "[INVALID REQUEST] :: Method arguments not valid!";
    }


}
