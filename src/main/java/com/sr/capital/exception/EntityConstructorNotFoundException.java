package com.sr.capital.exception;

import com.sr.capital.exception.custom.CustomException;

public class EntityConstructorNotFoundException extends CustomException {

    public EntityConstructorNotFoundException() {
        super("Entity Constructor not found for the request!");
    }
}
