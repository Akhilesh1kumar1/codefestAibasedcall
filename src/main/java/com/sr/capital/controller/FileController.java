package com.sr.capital.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.dto.request.file.FileUploadResponseDTO;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.omunify.core.util.Constants.Headers.TENANT_ID;
import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.PRESIGNED_URL_GENERATION;

@RestController
@RequestMapping("/api/file-upload")
@RequiredArgsConstructor
@Validated
public class FileController {


    final FileUploadService fileService;

    @PostMapping(value = "/presigned-url")
    public GenericResponse<FileUploadResponseDTO> generatePreSignedUrl(@RequestHeader(TENANT_ID) String tenantId,
                                                                       @RequestBody @Valid FileUploadRequestDTO fileUploadRequestDto) {
        String preSignedUrl = fileService.generatePreSignedUrl(fileUploadRequestDto, tenantId, RequestData.getUserId());
        return ResponseBuilderUtil.getResponse(new FileUploadResponseDTO(preSignedUrl), SUCCESS,
                PRESIGNED_URL_GENERATION, HttpStatus.SC_OK);
    }

    @PostMapping("/acknowledge")
    public GenericResponse<Object> acknowledgeFileUpload(@RequestBody FileUploadRequestDTO fileDetails) throws CustomException, JsonProcessingException {

        fileService.acknowledgeFile(fileDetails);

        return ResponseBuilderUtil.getResponse(null, SUCCESS,
                PRESIGNED_URL_GENERATION, HttpStatus.SC_OK);    }

}
