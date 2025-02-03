package com.sr.capital.controller;

import com.amazonaws.HttpMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.dto.response.FileUploadDataDTO;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.ACKNOWLEDGED_SUCCESSFULLY;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.PRESIGNED_URL_GENERATION;

@RestController
@RequestMapping("/api/file-upload")
@RequiredArgsConstructor
@Validated
public class FileController {


    final FileUploadService fileService;

    @PostMapping(value = "/presigned-url")
    public GenericResponse<Map<String, String>> generatePreSignedUrl(@RequestBody @Valid FileUploadRequestDTO fileUploadRequestDto) {
        //Todo :: remove this line
        fileUploadRequestDto.setCorrelationId(RequestData.getCorrelationId());
        Map<String, String> preSignedUrl = fileService.generatePreSignedUrl(fileUploadRequestDto, RequestData.getTenantId(), RequestData.getUserId(), HttpMethod.PUT);
        return ResponseBuilderUtil.getResponse(preSignedUrl, SUCCESS,
                PRESIGNED_URL_GENERATION, HttpStatus.SC_OK);
    }
    @PostMapping(value = "/download-doc-url")
    public GenericResponse<String> downloadDoc(@RequestBody @Valid FileUploadRequestDTO fileUploadRequestDto) {
        String preSignedUrl = fileService.generateDownloadPreSignedUrl(fileUploadRequestDto, RequestData.getTenantId(), RequestData.getUserId(), HttpMethod.GET);
        return ResponseBuilderUtil.getResponse(preSignedUrl, SUCCESS,
                PRESIGNED_URL_GENERATION, HttpStatus.SC_OK);
    }

    @PostMapping("/acknowledge")
    public GenericResponse<Object> acknowledgeFileUpload(@RequestBody FileUploadRequestDTO fileDetails) throws CustomException, JsonProcessingException {

        fileService.acknowledgeFile(fileDetails);

        return ResponseBuilderUtil.getResponse(null, SUCCESS,
                ACKNOWLEDGED_SUCCESSFULLY, HttpStatus.SC_OK);    }

    @GetMapping("/get-data")
    public GenericResponse<List<FileUploadDataDTO>> acknowledgeFileUpload() throws CustomException, JsonProcessingException {
        return ResponseBuilderUtil.getResponse(fileService.getUploadedFileDetails(), SUCCESS,
                ACKNOWLEDGED_SUCCESSFULLY, HttpStatus.SC_OK);
    }
    @PostMapping("/search")
    public GenericResponse<Page<FileUploadDataDTO>> searchByUploadedBy(@RequestParam(name = "uploaded_by") String uploadedBy,
                                                                       @RequestParam(name = "page_number", required = false) Integer pageNumber,
                                                                       @RequestParam(name = "page_size", required = false) Integer pageSize )
            throws CustomException, JsonProcessingException {
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 20;
        return ResponseBuilderUtil.getResponse(fileService.searchByUserIdOrName(uploadedBy, pageNumber, pageSize), SUCCESS,
                ACKNOWLEDGED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

}
