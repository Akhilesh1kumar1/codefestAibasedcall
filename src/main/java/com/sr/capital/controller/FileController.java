package com.sr.capital.controller;

import com.amazonaws.HttpMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.omunify.core.util.Constants.Headers.TENANT_ID;
import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.PRESIGNED_URL_GENERATION;
import static org.apache.http.client.methods.RequestBuilder.put;

@RestController
@RequestMapping("/api/file-upload")
@RequiredArgsConstructor
@Validated
public class FileController {


    final FileUploadService fileService;

    @PostMapping(value = "/presigned-url")
    public GenericResponse<Map> generatePreSignedUrl(@RequestBody @Valid FileUploadRequestDTO fileUploadRequestDto) {
        String preSignedUrl = fileService.generatePreSignedUrl(fileUploadRequestDto, RequestData.getTenantId(), RequestData.getUserId());
        String getSignedUrl = fileService.generateUrl(fileUploadRequestDto, RequestData.getTenantId(), System.currentTimeMillis(), HttpMethod.PUT);
        return ResponseBuilderUtil.getResponse(new HashMap<>(){{put("uploadUrl", preSignedUrl);put("downloadUrl",getSignedUrl);}}, SUCCESS,
                PRESIGNED_URL_GENERATION, HttpStatus.SC_OK);
    }

    @PostMapping("/acknowledge")
    public GenericResponse<Object> acknowledgeFileUpload(@RequestBody FileUploadRequestDTO fileDetails) throws CustomException, JsonProcessingException {

        fileService.acknowledgeFile(fileDetails);

        return ResponseBuilderUtil.getResponse(null, SUCCESS,
                PRESIGNED_URL_GENERATION, HttpStatus.SC_OK);    }

}
