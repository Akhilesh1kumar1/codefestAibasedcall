package com.sr.capital.exception.custom;

public class ResponseConstructorNotFoundException extends CustomException {

    public ResponseConstructorNotFoundException() {
        super("Response Constructor not found for the request!");
    }
}
