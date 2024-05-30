package com.sr.capital.dto.request.file;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileFailureReport {

    @CsvBindByPosition(position = 0)
    String errorMessage;

    public FileFailureReport(Exception ex) {
        this.errorMessage = ex.getMessage();
    }
}
