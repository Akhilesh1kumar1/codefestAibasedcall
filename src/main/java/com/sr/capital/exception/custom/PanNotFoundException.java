package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.PAN_NOT_FOUND;

public class PanNotFoundException extends CustomException{

   public PanNotFoundException(){
        super(PAN_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
