package com.sr.capital.exception.custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomServiceException extends RuntimeException {
    public CustomServiceException(String message) {
        super(message);
        log.info(message);
    }
}
