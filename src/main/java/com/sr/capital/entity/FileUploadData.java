package com.sr.capital.entity;

import com.sr.capital.dto.request.file.FileConsumptionDataDTO;
import com.sr.capital.helpers.enums.FileProcessingStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "file_upload_data")
public class FileUploadData {

    @MongoId
    String id;

    @NotEmpty
    String fileName;

    @NotEmpty
    String correlationId;

    FileProcessingStatus status;
    String locationId;
    String sourceFileUrl;
    FileConsumptionDataDTO fileConsumptionDataDTO;
    String tenantId;
    Long uploadedBy;
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime lastModifiedAt;
}
