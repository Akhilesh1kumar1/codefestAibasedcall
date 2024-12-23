package com.sr.capital.handler;

import com.omunify.core.exceptions.CustomException;
import com.omunify.core.model.ErrorResponse;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.ExceptionsTranslator;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.stream.Collectors;

import static com.omunify.core.util.Constants.StatusEnum.ERROR;
import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.external.crif.Constant.Constant.*;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.REQUEST_FAILED;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    final ExceptionsTranslator exceptionsTranslator;

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<GenericResponse<?>> handleException(final CustomException exception) {
        return exceptionsTranslator.getResponseEntityForCustomException(exception, RequestData.getCorrelationId(),
                REQUEST_FAILED);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        return new ResponseEntity<>(getGenericResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(getGenericResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler(CRIFApiException.class)
    public ResponseEntity<GenericResponse> handleCRIFApiException(final CRIFApiException exception) {
        return new ResponseEntity<>(getGenericResponse(HttpStatus.NOT_ACCEPTABLE.value(), exception.getErrorDetails()), HttpStatus.ACCEPTED);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler(CRIFApiLimitExceededException.class)
    public GenericResponse<Object> handleCRIFApiLimitExceededException(final CRIFApiLimitExceededException exception) {
        return ResponseBuilderUtil.getResponse(new HashMap<>(){{put(DATA, null); put(STAGE, LIMIT_EXCEEDED);}}
                ,SUCCESS,
                REQUEST_SUCCESS, org.apache.http.HttpStatus.SC_OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<GenericResponse<?>> handleException(final Exception exception) {
        return exceptionsTranslator.getResponseEntityForException(exception, RequestData.getCorrelationId(), REQUEST_FAILED);
    }

    private static GenericResponse<Object> getGenericResponse(ConstraintViolationException e) {
        return GenericResponse.builder()
                .status(ERROR)
                .correlationId(RequestData.getCorrelationId())
                .message(REQUEST_FAILED)
                .error(ErrorResponse.ErrorResponseBuilder().message(e.getMessage()).build())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }


    private static GenericResponse<Object> getGenericResponse(int status, String message) {
        return GenericResponse.builder()
                .status(ERROR)
                .correlationId(RequestData.getCorrelationId())
                .message(REQUEST_FAILED)
                .error(ErrorResponse.ErrorResponseBuilder().message(message).build())
                .statusCode(status)
                .build();
    }
    private static GenericResponse<Object> getGenericResponse(MethodArgumentNotValidException e) {
        return GenericResponse.builder()
                .status(ERROR)
                .correlationId(RequestData.getCorrelationId())
                .message(REQUEST_FAILED)
                .error(ErrorResponse.ErrorResponseBuilder().message(e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","))).build())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
