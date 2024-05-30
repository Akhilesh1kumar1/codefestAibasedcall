package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.DECRYPTION_ERROR;


public class DecryptionException extends CustomException {
    public DecryptionException(){
        super(DECRYPTION_ERROR, HttpStatus.BAD_REQUEST);
    }
}
