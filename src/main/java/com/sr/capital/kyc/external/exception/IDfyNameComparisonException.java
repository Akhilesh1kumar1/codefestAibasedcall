package com.sr.capital.kyc.external.exception;


import com.sr.capital.exception.custom.CustomRuntimeException;
import com.sr.capital.helpers.enums.TaskType;
import org.springframework.http.HttpStatus;

public class IDfyNameComparisonException extends CustomRuntimeException {

    public IDfyNameComparisonException(TaskType taskType) {
        super("Not able to compare names in :: " + taskType, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
