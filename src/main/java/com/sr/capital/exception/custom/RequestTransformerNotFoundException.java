package com.sr.capital.exception.custom;

public class RequestTransformerNotFoundException extends CustomException {

    public RequestTransformerNotFoundException(){
        super("Request Transformer not found for the request!");
    }
}
