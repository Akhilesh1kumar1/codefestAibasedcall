package com.sr.capital.entity.primary;

import com.sr.capital.dto.request.file.FileConsumptionDataDTO;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import com.sr.capital.helpers.enums.FileProcessingStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "file_upload_data")
public class FileUploadData extends BaseDoc {

    @NotEmpty
    String fileName;

    @NotEmpty
    String correlationId;

    FileProcessingStatus status;

    FileConsumptionDataDTO fileConsumptionDataDTO;

    String tenantId;

    Long uploadedBy;

    String uploadedByUserName;
}
