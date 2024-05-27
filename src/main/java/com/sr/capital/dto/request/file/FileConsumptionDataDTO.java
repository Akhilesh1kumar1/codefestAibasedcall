package com.sr.capital.dto.request.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileConsumptionDataDTO {

    int totalRecords;
    int successRecords;
    int validationFailedRecords;
    int errorRecords;
    String errorFilePath;
    LocalDateTime processStartTime;
    LocalDateTime processEndTime;

    public FileConsumptionDataDTO(FileProcessingHelper helper, String failureReportPath) {
        this.totalRecords = helper.getTotalRows();
        this.successRecords = 0;
        this.validationFailedRecords = helper.getRowFailureReports().size();
        this.errorFilePath = failureReportPath;
        this.processStartTime = Instant.ofEpochMilli(helper.getProcessStartTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.processEndTime = Instant.ofEpochMilli(helper.getProcessEndTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public FileConsumptionDataDTO(String failureReportPath, FileProcessingHelper fileProcessingHelper,
                                  long startTime, long endTime) {
        this.totalRecords = fileProcessingHelper.getTotalRows();
        this.successRecords = 0;
        this.validationFailedRecords = fileProcessingHelper.getTotalRows();
        this.errorFilePath = failureReportPath;
        this.processStartTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.processEndTime = Instant.ofEpochMilli(endTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
