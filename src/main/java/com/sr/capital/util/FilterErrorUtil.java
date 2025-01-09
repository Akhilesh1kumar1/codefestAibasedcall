package com.sr.capital.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.exception.custom.CaptchaValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.omunify.core.util.Constants.StatusEnum.ERROR;
import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_FAILED;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

public class FilterErrorUtil {

    public static void onError(HttpServletResponse response, String err, HttpStatus httpStatus) {
        // Create the generic response object
        GenericResponse<?> genericResponse = ResponseBuilderUtil.getResponse(null
                ,ERROR, REQUEST_FAILED, 400);
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            // Serialize the response object into JSON
            String json = objectWriter.writeValueAsString(genericResponse);

            // Write the JSON response
            response.setStatus(httpStatus.value());
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(json);
        } catch (IOException e) {
            // Handle any IOExceptions while writing response
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
