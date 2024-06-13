package com.sr.capital.exception.custom;

public class ServiceEndpointNotFoundException extends CustomException {

    public ServiceEndpointNotFoundException() {
        super("Service End point not found!");
    }

}
