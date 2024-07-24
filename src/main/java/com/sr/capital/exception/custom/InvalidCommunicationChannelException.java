package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;


public class InvalidCommunicationChannelException extends CustomException {
    public InvalidCommunicationChannelException(){
        super("Communication Channel unavailable for this request! Please use other channels.", HttpStatus.FORBIDDEN);
    }
}
