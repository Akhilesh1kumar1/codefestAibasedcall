package com.sr.capital.dto.request.file;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileProcessDTO {

    String fileName;
    String filePath;
    String locationId;

    public FileProcessDTO(FileUploadRequestDTO fileUploadRequestDTO, String filePath, String locationId) {
        this.fileName = fileUploadRequestDTO.getFileName();
        this.filePath = filePath;
        this.locationId = locationId;
    }
}
