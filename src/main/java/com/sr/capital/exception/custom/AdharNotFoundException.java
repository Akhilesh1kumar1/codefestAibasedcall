package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.ADHAR_NOT_FOUND;

public class AdharNotFoundException extends CustomException{

    public AdharNotFoundException(){
        super(ADHAR_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
