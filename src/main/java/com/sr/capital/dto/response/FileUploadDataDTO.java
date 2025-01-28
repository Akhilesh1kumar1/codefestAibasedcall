package com.sr.capital.dto.response;

import com.sr.capital.dto.request.file.FileConsumptionDataDTO;
import com.sr.capital.helpers.enums.FileProcessingStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDataDTO {

    private String fileName;

    private String correlationId;

    private FileProcessingStatus status;

    private FileConsumptionDataDTO fileConsumptionData;

    private String tenantId;

    private Long uploadedBy;
}
