package com.sr.capital.dto.request.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileProcessingHelper {

    List<RowFailureReport> rowFailureReports = new ArrayList<>();
    String headers;
    int totalRows;
    long processStartTime;
    long processEndTime;

    public void incrementTotalRows(int i) {
        this.totalRows += i;
    }

    public void setProcessStartTimeAndEndTime(long processStartTime, long processEndTime) {
        this.processStartTime = processStartTime;
        this.processEndTime = processEndTime;
    }
}
