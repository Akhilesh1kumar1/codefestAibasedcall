package com.sr.capital.excelprocessor.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileConsumptionDataDTO;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.primary.FileUploadData;
import com.sr.capital.excelprocessor.model.LoanDetailsFieldFromExcel;
import com.sr.capital.excelprocessor.util.LoanDetailsConstants;
import com.sr.capital.repository.mongo.FileUploadDataRepository;
import com.sr.capital.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

import static com.sr.capital.helpers.enums.FileProcessingStatus.ACKNOWLEDGEMENT_DONE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelProcessingService {

    private final DataProcessService dataProcessService;
    private final AppProperties appProperties;
    private final FileUploadDataRepository fileUploadDataRepository;


    public List<LoanDetailsFieldFromExcel> processExcel(FileUploadRequestDTO processUploadDataMessage) throws IOException {
        LocalDateTime processStartTime = LocalDateTime.now();
        List<LoanDetailsFieldFromExcel> loanDetailsList = new ArrayList<>();
        int lastColumIndex = 0;

//        String excelFilePath = "/home/akhileshkumar/IdeaProjects/capital/src/main/resources/templates/sheet.xlsx";
//        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
//             Workbook workbook = new XSSFWorkbook(fis)) {

        log.info("Started Fetching from s3 bucket");
        InputStream inputStream = S3Util.downloadObjectToFile(appProperties.getBucketName(), processUploadDataMessage.getFileName());
        log.info("file fetched Starting processing");
        log.info(String.valueOf("Input Stream " + inputStream != null + " FIleName " + processUploadDataMessage.getFileName()));
        File file = null;
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            int lastNonEmptyRow = findLastNonEmptyRow(sheet);

            // Create a map of column names to their indices
            Map<String, Integer> columnIndexMap = new HashMap<>();
            for (Cell cell : headerRow) {
                columnIndexMap.put(cell.getStringCellValue().toLowerCase().trim(), cell.getColumnIndex());
                lastColumIndex = cell.getColumnIndex();
            }
            columnIndexMap.forEach((key, value) -> log.info(key + " " + value));
            headerRow.createCell(lastColumIndex++).setCellValue(LoanDetailsConstants.STATUS);
            headerRow.createCell(lastColumIndex).setCellValue(LoanDetailsConstants.MESSAGE);
            log.info("lastNonEmptyRow " + lastNonEmptyRow);
            // Process rows
            for (int i = 1; i <= lastNonEmptyRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                LoanDetailsFieldFromExcel loanDetails = new LoanDetailsFieldFromExcel();
                try {
                    loanDetails.setRowId(i);
                    setValuesFromDoc(loanDetails, columnIndexMap, row);
                    loanDetailsList.add(loanDetails);
                } catch (Exception e) {
                    row.createCell(row.getLastCellNum()).setCellValue("Failed");
                    row.createCell(row.getLastCellNum()).setCellValue(e.getMessage());
                }
            }

            loanDetailsList.forEach(d -> log.info(d.getCompanyName()));

            processData(loanDetailsList); // Custom post-processing


            log.info("update doc based on status of row");
            for (LoanDetailsFieldFromExcel loanDetailsFieldFromExcel : loanDetailsList) {
                sheet = workbook.getSheetAt(0);
                Row row = sheet.getRow(loanDetailsFieldFromExcel.getRowId());
                if (loanDetailsFieldFromExcel.getCurrentStatus() != null) {

                    row.createCell(lastColumIndex).setCellValue(loanDetailsFieldFromExcel.getMessage());
                    row.createCell(lastColumIndex - 1).setCellValue(loanDetailsFieldFromExcel.getCurrentStatus());

                } else {
                    row.createCell(lastColumIndex - 1).setCellValue(LoanDetailsConstants.SUCCESS);
                }
            }

            log.info("upload to s3");
            file = convertWorkbookToFile(workbook, processUploadDataMessage.getFileName());
            S3Util.uploadFileToS3(appProperties.getBucketName(), processUploadDataMessage.getFileName(), file);
            boolean isDeleted = file.delete();
            log.info("Is Temp File Deleted ?" + isDeleted);
//            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
//                workbook.write(fos);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            log.info("Upload updated Data");
        } catch (IOException e) {
            log.error(e.getMessage() + e);
            if (file != null) {
                file.delete();
            }

        }

        LocalDateTime processEndTime = LocalDateTime.now();
        updateDataInDb(loanDetailsList, RequestData.getUserId(), RequestData.getTenantId(), processUploadDataMessage.getFileName(), processStartTime, processEndTime);
        log.info("Data updated in db");

        return loanDetailsList;
    }

    private void updateDataInDb(List<LoanDetailsFieldFromExcel> loanDetailsList, Long userId, String fileName, String tenantId, LocalDateTime processStartTime, LocalDateTime processEndTime) {
        log.info("Start Updating Db");
        FileUploadData fileUploadOldData = fileUploadDataRepository.findByTenantIdAndUploadedByAndFileName(tenantId, userId, fileName);
        if (fileUploadOldData != null) {
            long failedCount = loanDetailsList.stream().filter(d -> d.getCurrentStatus().equals(LoanDetailsConstants.FAILED)).count();
            long successCount = loanDetailsList.size() - failedCount;
            fileUploadOldData.setStatus(ACKNOWLEDGEMENT_DONE);
            fileUploadOldData.setFileConsumptionDataDTO(new FileConsumptionDataDTO(loanDetailsList.size(), successCount, failedCount, processStartTime, processEndTime));
            fileUploadDataRepository.save(fileUploadOldData);
        }

    }

    public static File convertWorkbookToFile(Workbook workbook, String fileName) throws IOException {
        // Create a temporary file
        File tempFile = createTempFile(fileName.substring(0,fileName.lastIndexOf("."))
                ,fileName.substring(fileName.lastIndexOf(".")));

        // Write the workbook data to the file
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            workbook.write(fileOutputStream);
        }

        // Close the workbook
        workbook.close();

        return tempFile;
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix).toFile();
    }
    private void setValuesFromDoc(LoanDetailsFieldFromExcel loanDetails, Map<String, Integer> columnIndexMap, Row row) {
        loanDetails.setShipRocketApplicationId(getCellValue(row, columnIndexMap, LoanDetailsConstants.SHIPROCKET_APPLICATION_ID));
        loanDetails.setDateOfInitiation(getDateCellValue(row, columnIndexMap, LoanDetailsConstants.DATE_OF_INITIATION));
        loanDetails.setLoanType(getCellValue(row, columnIndexMap, LoanDetailsConstants.LOAN_TYPE));
        loanDetails.setCompanyId(getIntegerCellValue(row, columnIndexMap, LoanDetailsConstants.COMPANY_ID));
        loanDetails.setCompanyName(getCellValue(row, columnIndexMap, LoanDetailsConstants.COMPANY_NAME));
        loanDetails.setCompanyTier(getCellValue(row, columnIndexMap, LoanDetailsConstants.COMPANY_TIER));
        loanDetails.setVendorLoanId(getIntegerCellValue(row, columnIndexMap, LoanDetailsConstants.VENDOR_LOAN_ID));
        loanDetails.setLoanVendorName(getCellValue(row, columnIndexMap, LoanDetailsConstants.LOAN_VENDOR_NAME));
        loanDetails.setShipRocketLoanStatus(getCellValue(row, columnIndexMap, LoanDetailsConstants.SHIPROCKET_LOAN_STATUS));
        loanDetails.setVendorLoanStatus(getCellValue(row, columnIndexMap, LoanDetailsConstants.VENDOR_LOAN_STATUS));
        loanDetails.setSanctionDate(getDateCellValue(row, columnIndexMap, LoanDetailsConstants.SANCTION_DATE));
        loanDetails.setDisbursementDate(getDateCellValue(row, columnIndexMap, LoanDetailsConstants.DISBURSEMENT_DATE));
        loanDetails.setSanctionAmount(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.SANCTION_AMOUNT));
        loanDetails.setSanctionLoanTenure(getIntegerCellValue(row, columnIndexMap, LoanDetailsConstants.SANCTION_LOAN_TENURE));
        loanDetails.setSanctionLoanROI(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.SANCTION_LOAN_ROI));
        loanDetails.setDisbursementAmount(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.DISBURSEMENT_AMOUNT));
        loanDetails.setDisbursementLoanTenure(getIntegerCellValue(row, columnIndexMap, LoanDetailsConstants.DISBURSEMENT_LOAN_TENURE));
        loanDetails.setDisbursementLoanROI(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.DISBURSEMENT_LOAN_ROI));
        loanDetails.setTotalRecoverableAmount(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_RECOVERABLE_AMOUNT));
        loanDetails.setMonthlyEMIAmount(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.MONTHLY_EMI_AMOUNT));
        loanDetails.setNextEMIDate(getDateCellValue(row, columnIndexMap, LoanDetailsConstants.NEXT_EMI_DATE));
        loanDetails.setLastEMIDate(getDateCellValue(row, columnIndexMap, LoanDetailsConstants.LAST_EMI_DATE));
        loanDetails.setTotalRepaymentAmountReceived(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_REPAYMENT_AMOUNT_RECEIVED));
        loanDetails.setAumCollection(getCellValue(row, columnIndexMap, LoanDetailsConstants.AUM_COLLECTION));
        loanDetails.setPrincipalOutstanding(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.PRINCIPAL_OUTSTANDING));
        loanDetails.setInterestOutstanding(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.INTEREST_OUTSTANDING));
        loanDetails.setTotalPrincipalPaid(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_PRINCIPAL_PAID));
        loanDetails.setTotalInterestPaid(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_INTEREST_PAID));
        loanDetails.setTotalLpiPaid(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_LPI_PAID));
        loanDetails.setTotalBouncePaid(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_BOUNCE_PAID));
        loanDetails.setTotalPaidAmount(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_PAID_AMOUNT));
        loanDetails.setPrincipalOverdue(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.PRINCIPAL_OVERDUE));
        loanDetails.setInterestOverdue(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.INTEREST_OVERDUE));
        loanDetails.setLpiOverdue(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.LPI_OVERDUE));
        loanDetails.setBounceOverdue(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.BOUNCE_OVERDUE));
        loanDetails.setTotalOverdueAmount(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.TOTAL_OVERDUE_AMOUNT));
        loanDetails.setDpd(getIntegerCellValue(row, columnIndexMap, LoanDetailsConstants.DPD));
        loanDetails.setDpdBucket(getCellValue(row, columnIndexMap, LoanDetailsConstants.DPD_BUCKET));
        loanDetails.setAmountCollectedLast30Days(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.AMOUNT_COLLECTED_LAST_30_DAYS));
        loanDetails.setAmountCollectedLast60Days(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.AMOUNT_COLLECTED_LAST_60_DAYS));
        loanDetails.setRepaymentMtd(getCellValue(row, columnIndexMap, LoanDetailsConstants.REPAYMENT_MTD));
        loanDetails.setAmountDueCurrMonth(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.AMOUNT_DUE_CURR_MONTH));
        loanDetails.setExcessPayment(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.EXCESS_PAYMENT));
        loanDetails.setLoanStatus(getCellValue(row, columnIndexMap, LoanDetailsConstants.LOAN_STATUS));
        loanDetails.setWaiverAmount(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.WAIVER_AMOUNT));
        loanDetails.setAmountDueTillDate(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.AMOUNT_DUE_TILL_DATE));
        loanDetails.setProgramCode(getCellValue(row, columnIndexMap, LoanDetailsConstants.PROGRAM_CODE));
        loanDetails.setCity(getCellValue(row, columnIndexMap, LoanDetailsConstants.CITY));
        loanDetails.setState(getCellValue(row, columnIndexMap, LoanDetailsConstants.STATE));
        loanDetails.setPincode(getCellValue(row, columnIndexMap, LoanDetailsConstants.PINCODE));
        loanDetails.setProcessingFeeRate(getNumericCellValue(row, columnIndexMap, LoanDetailsConstants.PROCESSING_FEE_RATE));
        loanDetails.setEver30Plus(getCellValue(row, columnIndexMap, LoanDetailsConstants.EVER_30_PLUS));
        loanDetails.setEver60Plus(getCellValue(row, columnIndexMap, LoanDetailsConstants.EVER_60_PLUS));
        loanDetails.setEver90Plus(getCellValue(row, columnIndexMap, LoanDetailsConstants.EVER_90_PLUS));
        loanDetails.setEver180Plus(getCellValue(row, columnIndexMap, LoanDetailsConstants.EVER_180_PLUS));
        loanDetails.setLastEmiPaidDate(getDateCellValue(row, columnIndexMap, LoanDetailsConstants.LAST_EMI_PAID_DATE));
        loanDetails.setLastPaymentMode(getCellValue(row, columnIndexMap, LoanDetailsConstants.LAST_PAYMENT_MODE));
        loanDetails.setLastPaymentAmount(getBigDecimalCellValue(row, columnIndexMap, LoanDetailsConstants.LAST_PAYMENT_AMOUNT));
        loanDetails.setFutureEmiPendingCount(getIntegerCellValue(row, columnIndexMap, LoanDetailsConstants.FUTURE_EMI_PENDING_COUNT));
        loanDetails.setPanNumber(getCellValue(row, columnIndexMap, LoanDetailsConstants.PAN_NUMBER));
        loanDetails.setGstNumber(getCellValue(row, columnIndexMap, LoanDetailsConstants.GST_NUMBER));
        loanDetails.setUdhyam(getCellValue(row, columnIndexMap, LoanDetailsConstants.UDHYAM));
        loanDetails.setUdhyamNumber(getCellValue(row, columnIndexMap, LoanDetailsConstants.UDHYAM_NUMBER));

    }

    private void processData(List<LoanDetailsFieldFromExcel> loanDetailsList) {
        validateData(loanDetailsList);
        for (LoanDetailsFieldFromExcel loanDetailsFieldFromExcel : loanDetailsList) {
            if (loanDetailsFieldFromExcel.getCurrentStatus() == null || !loanDetailsFieldFromExcel.getCurrentStatus().equals(LoanDetailsConstants.FAILED)) {
                dataProcessService.saveDataIntoDb(loanDetailsFieldFromExcel);
            }
        }

    }

    private String getCellValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex == null || row.getCell(columnIndex) == null) return null;
        return row.getCell(columnIndex).getCellType() == CellType.STRING ? row.getCell(columnIndex).getStringCellValue() : String.valueOf(row.getCell(columnIndex));
    }

    private Double getNumericCellValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex == null || row.getCell(columnIndex) == null) return null;
        return row.getCell(columnIndex).getNumericCellValue();
    }
    private BigDecimal getBigDecimalCellValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex == null || row.getCell(columnIndex) == null) return null;
        return BigDecimal.valueOf(row.getCell(columnIndex).getNumericCellValue());
    }

    private Integer getIntegerCellValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Double value = getNumericCellValue(row, columnIndexMap, columnName);
        return value != null ? value.intValue() : null;
    }

    private Date getDateCellValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer columnIndex = columnIndexMap.get(columnName);
        if (columnIndex == null || row.getCell(columnIndex) == null) return null;
        return row.getCell(columnIndex).getDateCellValue();
    }

    private int findLastNonEmptyRow(Sheet sheet) {
        int lastRow = sheet.getLastRowNum();
        for (int i = lastRow; i >= 0; i--) {
            Row row = sheet.getRow(i);
            if (!isRowEmpty(row)) {
                return i;
            }
        }
        return 0; // Default to the header row if all rows are empty
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
    private void validateData(List<LoanDetailsFieldFromExcel> loanDetailsList) {
        loanDetailsList.forEach(d -> {
            Map<String, String> validateFieldMessage = validateLoanDetails(d);
            if (validateFieldMessage.containsKey(LoanDetailsConstants.STATUS)) {
                log.info("Failed Row id " + d.getRowId() + " Msg " + validateFieldMessage);
                d.setCurrentStatus(LoanDetailsConstants.FAILED);
                d.setMessage(validateFieldMessage.toString());
            }
        });
    }



    // Helper method to safely get string value from a cell
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return cell.getStringCellValue();
    }

    public static Map<String, String> validateLoanDetails(LoanDetailsFieldFromExcel loanDetails) {
        Map<String, String> validationErrors = new HashMap<>();

        for (String fieldName : LoanDetailsConstants.MANDATORY_FIELDS) {
            try {
                Field field = LoanDetailsFieldFromExcel.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(loanDetails);

                // Check if the field is null or empty (for Strings)
                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    validationErrors.put(fieldName, "This field is mandatory and cannot be null or empty.");

                    if (!validationErrors.containsKey(LoanDetailsConstants.STATUS)) {
                        validationErrors.put(LoanDetailsConstants.STATUS, "ERROR");
                    }
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                validationErrors.put(fieldName, "Error accessing field: " + e.getMessage());
                validationErrors.put(LoanDetailsConstants.STATUS, "ERROR");
            }
        }

        return validationErrors;
    }
}