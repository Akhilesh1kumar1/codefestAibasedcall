package com.sr.capital.exception.custom;

import com.sr.capital.exception.ExceptionsDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends Exception {

    private String message;
    private HttpStatus code;

    public CustomException(){
        this.message = ExceptionsDetails.INTERNAL_ERROR.MESSAGE;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(String message){
        super(message);
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(String message, HttpStatus code){
        super(message);
        this.message = message;
        this.code = code;
    }
}
