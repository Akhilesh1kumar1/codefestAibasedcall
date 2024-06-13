package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class UnsupportedMediaType extends CustomException {
    public UnsupportedMediaType() {
        super("INVALID REQUEST :: File Type not supported! Please try again with a valid file format. Supported Types: [JPG, JPEG, PNG, PDF]", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
