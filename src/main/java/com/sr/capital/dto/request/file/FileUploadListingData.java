package com.sr.capital.dto.request.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadListingData {

    String process;
    String sourceFileLink;
    String fileName;
    String startTime;
    String endTime;
    int totalRecords;
    int processedRecords;
    String progressState;
    String errorFileLink;
    String errorFileName;
    String uploadedBy;
}
