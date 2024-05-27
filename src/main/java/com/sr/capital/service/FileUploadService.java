package com.sr.capital.service;


import com.sr.capital.dto.request.file.FileUploadRequestDTO;

public interface FileUploadService {

    String generatePreSignedUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId);

    void acknowledgeFile(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId);
}
