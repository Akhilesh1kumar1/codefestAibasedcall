package com.sr.capital.excelprocessor.event;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.primary.FileUploadData;
import com.sr.capital.excelprocessor.model.LoanDetailsFieldFromExcel;
import com.sr.capital.excelprocessor.service.ExcelProcessingService;
import com.sr.capital.repository.mongo.FileUploadDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessUploadedDataEventHandlerServiceImpl {

    private final ExcelProcessingService excelProcessingService;
    private final FileUploadDataRepository fileUploadDataRepository;

    public void processData(FileUploadRequestDTO payload) throws IOException {
        FileUploadData fileUpload = fileUploadDataRepository.findByTenantIdAndCorrelationId(RequestData.getTenantId(), payload.getCorrelationId());
        if (fileUpload != null) {
            List<LoanDetailsFieldFromExcel> loanDetailsFieldFromExcels = excelProcessingService.processExcel(payload);
        }
    }
}
