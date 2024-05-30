package com.sr.capital.validation;

import com.omunify.core.util.ExceptionUtils;

import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.FileUploadData;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.omunify.core.util.Constants.GlobalErrorEnum.BAD_REQUEST;

import static com.sr.capital.helpers.constants.Constants.FILE_CONTENT_TYPE_MAP;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.*;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;
import static com.sr.capital.helpers.enums.FileProcessingStatus.ACKNOWLEDGEMENT_DONE;

public class FileValidator {

    private FileValidator() {
        throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
    }

    public static void validateFileUploadRequest(FileUploadRequestDTO fileUploadRequestDto) {

        String fileExtension = FilenameUtils.getExtension(fileUploadRequestDto.getFileName());

        if (StringUtils.isEmpty(fileExtension)) {
            ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), EMPTY_FILE_TYPE, HttpStatus.BAD_REQUEST);
        }

        if (!FILE_CONTENT_TYPE_MAP.containsKey(fileExtension)) {
            ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), UNSUPPORTED_FILE_TYPE, HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateAcknowledgementRequest(FileUploadData fileUploadDataDto,
                                                      FileUploadRequestDTO fileUploadRequestDto) {

        if (StringUtils.isEmpty(fileUploadRequestDto.getCorrelationId())) {
            ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), EMPTY_CORRELATION_ID, HttpStatus.BAD_REQUEST);
        }

        if (fileUploadDataDto == null) {
            ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), INVALID_CORRELATION_ID, HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.equals(fileUploadDataDto.getFileName(), fileUploadRequestDto.getFileName())) {
            ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), FILE_NAME_MISMATCH, HttpStatus.BAD_REQUEST);
        }

        if (fileUploadDataDto.getStatus().equals(ACKNOWLEDGEMENT_DONE)) {
            ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), STATUS_MISMATCH, HttpStatus.BAD_REQUEST);
        }
    }

    //TODO
    public static void validateFileHeaders(List<String> headersList) {
      /*  if (!INVENTORY_FILE_HEADERS.equals(headersList)) {
            throw new FileProcessingException(HEADER_SEQUENCE_MISMATCH.getErrorMessage());
        }*/
    }
}
