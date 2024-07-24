package com.sr.capital.kyc.external.exception;


import com.sr.capital.exception.custom.CustomRuntimeException;
import com.sr.capital.helpers.enums.TaskType;
import org.springframework.http.HttpStatus;

public class KarzaNameComparisonException extends CustomRuntimeException {

    public KarzaNameComparisonException(TaskType taskType) {
        super("Not able to compare names in :: " + taskType, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
