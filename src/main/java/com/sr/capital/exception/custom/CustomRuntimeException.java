package com.sr.capital.exception.custom;

import com.sr.capital.exception.ExceptionsDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomRuntimeException extends RuntimeException {

    private String message;
    private HttpStatus code;

    public CustomRuntimeException(){
        this.message = ExceptionsDetails.DEFAULT_MESSAGE;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomRuntimeException(String message){
        super(message);
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomRuntimeException(String message, HttpStatus code){
        super(message);
        this.message = message;
        this.code = code;
    }
}
