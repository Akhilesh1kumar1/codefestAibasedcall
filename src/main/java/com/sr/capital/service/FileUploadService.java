package com.sr.capital.service;


import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {

    String generatePreSignedUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId);

    void acknowledgeFile(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId);

    String downloadAndAddFileToZip(List<KycDocDetails<?>> docDetails) ;
}
