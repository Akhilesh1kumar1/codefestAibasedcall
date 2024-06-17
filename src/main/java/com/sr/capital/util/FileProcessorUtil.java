package com.sr.capital.util;


import com.sr.capital.service.FileProcessor;
import com.sr.capital.service.impl.CsvFileProcessor;
import com.sr.capital.service.impl.ExcelFileProcessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sr.capital.helpers.constants.Constants.FileContentTypes.*;


@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileProcessorUtil {

    final CsvFileProcessor csvFileProcessor;
    final ExcelFileProcessor excelFileProcessor;

    public FileProcessor getFileProcessor(String fileType) {
        return switch (fileType) {
            case CSV_EXTENSION -> csvFileProcessor;
            case XLS_EXTENSION, XLSX_EXTENSION -> excelFileProcessor;
            default -> {
                log.error("Invalid file type: {}", fileType);
                yield null;
            }
        };
    }
}
