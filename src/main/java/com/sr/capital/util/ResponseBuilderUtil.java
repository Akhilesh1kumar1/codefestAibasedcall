package com.sr.capital.util;

import com.omunify.core.model.ErrorResponse;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.response.FileUploadDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;


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