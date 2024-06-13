package com.sr.capital.kyc.dto.request;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
public class FileDetails {

    private MultipartFile file;

    private String fileName;

    private UUID fileId;

    private String preSignedUri;
}
