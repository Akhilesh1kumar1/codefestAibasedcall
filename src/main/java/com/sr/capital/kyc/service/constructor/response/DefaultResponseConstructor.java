package com.sr.capital.kyc.service.constructor.response;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

@Service
public class DefaultResponseConstructor implements ResponseConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {
       /* String message;
        if(input != null){
            message = (String) input;
        } else {
            message = "OK!";
        }*/
        GenericResponse<T> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData((T)input);

        return new ResponseEntity<>(
            response,
            HttpStatus.OK
        );
    }

}
