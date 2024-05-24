package com.sr.capital.util;

import com.omunify.core.model.ErrorResponse;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.dto.RequestData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;



@Component
public class ResponseBuilderUtil {


    public static <T> GenericResponse<T> getResponse(T response, Constants.StatusEnum status, String message, int statusCode) {
        return GenericResponse.<T>builder()
                .data(response)
                .correlationId(RequestData.getCorrelationId())
                .status(status)
                .message(message)
                .statusCode(statusCode)
                .build();
    }
}