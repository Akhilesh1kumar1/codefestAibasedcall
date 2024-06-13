package com.sr.capital.util;

import com.omunify.core.util.Constants;
import com.omunify.core.util.ExceptionUtils;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sr.capital.entity.FileUploadData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.service.FileProcessor;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static com.opencsv.ICSVWriter.DEFAULT_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.Separators.SLASH_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.INVALID_FILE_DIRECTORY;

@Component
@Slf4j
public class CsvUtils  {

    public <T> byte[] createCSV(List<T> t, String headers) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             OutputStreamWriter streamWriter = new OutputStreamWriter(stream)) {
            StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(streamWriter).withSeparator(DEFAULT_SEPARATOR).build();
            streamWriter.append(headers);
            sbc.write(t);
            streamWriter.flush();
            return stream.toByteArray();
        }
    }

    public <T> byte[] createCSV(T t, String headers) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return createCSV(Collections.singletonList(t), headers);
    }

    public static boolean deleteFile(String fileFolder, File file) {
        try {
            String canonicalDestinationPath = file.getCanonicalPath();
            if (!canonicalDestinationPath.contains(fileFolder)) {
                throw ExceptionUtils.customException(Constants.GlobalErrorEnum.BAD_REQUEST.getCode(),
                        String.format(INVALID_FILE_DIRECTORY, file.getCanonicalPath()), Constants.GlobalErrorEnum.BAD_REQUEST.getHttpStatus());
            }
        } catch (IOException e) {
            log.error("Error while checking canonical path for file : {}, error : {}", file.getName(), e.getMessage());
            throw ExceptionUtils.customException(Constants.GlobalErrorEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(),
                    Constants.GlobalErrorEnum.BAD_REQUEST.getHttpStatus());
        }
        return file.delete();
    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile, String destinationFolder, String destinationPath) {
        File destinationFile = new File(destinationPath);
        try {
            String canonicalDestinationPath = destinationFile.getCanonicalPath();
            if (!canonicalDestinationPath.contains(destinationFolder)) {
                throw ExceptionUtils.customException(Constants.GlobalErrorEnum.BAD_REQUEST.getCode(),
                        String.format(INVALID_FILE_DIRECTORY, destinationPath), Constants.GlobalErrorEnum.BAD_REQUEST.getHttpStatus());
            }
        } catch (IOException e) {
            log.error("Error while checking canonical path for file : {}, error : {}", destinationPath, e.getMessage());
            throw ExceptionUtils.customException(Constants.GlobalErrorEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(),
                    Constants.GlobalErrorEnum.BAD_REQUEST.getHttpStatus());
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.createDirectories(Paths.get(destinationFolder));
            Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error while downloading the multipart file : {}", multipartFile.getOriginalFilename());
            throw ExceptionUtils.customException(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                    String.format("Error while downloading the multipart file : %s", multipartFile.getOriginalFilename()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return destinationFile;
    }

    public static File createFile(String s3Path) {
        String directoryPath = s3Path.substring(0, s3Path.lastIndexOf(SLASH_SEPARATOR));
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directoryPath, s3Path.substring(s3Path.lastIndexOf("/") + 1));
    }

    public static String getStringValueWithDefault(Boolean value) {
        return String.valueOf(value != null && value);
    }

    public static boolean isNextPageExists(int pageNumber, int pageSize, long totalHits) {
        int totalPage = (pageNumber + 1) * pageSize;
        return totalPage < totalHits;
    }

    public static BigDecimal setDecimalScale(int scale, BigDecimal value) {
        return value == null ? value : value.setScale(scale, RoundingMode.HALF_UP);
    }

    public static String addCdnBeforeS3Path(String s3CdnUrl, String s3Path) {
        return StringUtils.isNotEmpty(s3Path) ? s3CdnUrl + SLASH_SEPARATOR + s3Path : s3Path;
    }

    public List<Map<String,String>> getCsvContent(String filePath,@Nullable Boolean missingBodyValues) throws CustomException {

        try {
            missingBodyValues = Optional.ofNullable(missingBodyValues).orElse(false);
            return readFile(filePath,missingBodyValues);
        } catch(IOException e) {
            throw new CustomException("Invalid file : file parsing exception",HttpStatus.BAD_REQUEST);
        }
    }

    private List<Map<String,String>> readFile(String filePath, Boolean missingBodyValues) throws CustomException,IOException {

        List<Map<String, String>> csvData = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        try {
            String firstLine = br.readLine();
            String bodyLine;
            String[] header = readFirstLine(firstLine);
            while ((bodyLine = br.readLine()) != null) {
                String[] bodyArr = readBody(bodyLine);
                if (header.length != bodyArr.length) {
                    if(missingBodyValues && bodyArr.length < header.length){
                        bodyArr = setRemainingEmpty(header.length,bodyArr);
                    }else
                        throw new CustomException("Invalid Csv",HttpStatus.BAD_REQUEST);
                }
                csvData.add(createBodyMap(header, bodyArr));
            }
        }
        finally {
            br.close();
        }
        return csvData;
    }

    private String[] readFirstLine(String firstLine) throws CustomException {
        if(firstLine == null) {
            throw new CustomException("Empty Csv File",HttpStatus.BAD_REQUEST);
        }
        return firstLine.split(",");
    }

    private String[] readBody(String bodyLines) {
        return bodyLines.split(",");
    }

    private Map<String,String> createBodyMap(String[] header, String[] body) {
        Map<String,String> bodyMap = new HashMap<>();
        for(int i = 0; i<header.length;i++) {
            bodyMap.put(header[i].replaceAll("\"",""), (body[i] == null ?  "":body[i]));
        }
        return bodyMap;
    }

    private  String[] setRemainingEmpty(int headerLength, String[] bodyArr){
        int startIndex = bodyArr.length;
        String[] newArr = new String[headerLength];
        for(int i = 0; i<startIndex;i++){
            newArr[i] = bodyArr[i];
        }

        for(int i = startIndex; i<headerLength;i++){
            newArr[i] = "";
        }
        return newArr;

    }

    public static File convertMultiPartToFile(FileDetails fileDetails) throws IOException {
        File convFile = new File(fileDetails.getFileName());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(fileDetails.getFile().getBytes());
        fos.close();
        return convFile;
    }

    public static File extractAndSaveSinglePageFromPdf(FileDetails fileDetails, Integer pageNo) throws IOException {

        File temp = convertMultiPartToFile(fileDetails);
        PDDocument document = PDDocument.load(temp);
        String docName = fileDetails.getFileName();
        fileDetails.setFileName("ONE-PAGER-" + docName);
        PDDocument buffer = new PDDocument();

        try {
            PDPage pdPage = document.getPage(pageNo);
            buffer.addPage(pdPage);
            buffer.save(fileDetails.getFileName());
        } finally {
            buffer.close();
            document.close();
            Files.deleteIfExists(Path.of(docName));
        }

        return new File(fileDetails.getFileName());
    }
}
