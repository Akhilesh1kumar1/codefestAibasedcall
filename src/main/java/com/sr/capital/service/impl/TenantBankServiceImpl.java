package com.sr.capital.service.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.dto.request.EnachLinkingRequestDto;
import com.sr.capital.dto.request.VerifyBankDetails;
import com.sr.capital.dto.response.EnachLinkingResponseDto;
import com.sr.capital.dto.response.TenantBankResponseDto;
import com.sr.capital.entity.EnachLinkingEntity;
import com.sr.capital.entity.TenantBankDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.repository.EnachLinkingRepository;
import com.sr.capital.repository.TenantBankDetailsRepository;
import com.sr.capital.service.TenantBankService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.CsvUtils;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sr.capital.helpers.constants.Constants.Separators.EXTENSION_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.Separators.SLASH_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.BANK_IMAGE_FOLDER_NAME;

@Service
@AllArgsConstructor
public class TenantBankServiceImpl implements TenantBankService {

    final TenantBankDetailsRepository tenantBankDetailsRepository;

    final AppProperties appProperties;

    final RequestValidationStrategy requestValidationStrategy;

    final EnachLinkingRepository enachLinkingRepository;
    @Override
    public TenantBankResponseDto addBankDetails(BankDetailsRequestDto bankDetailsRequestDto, MultipartFile document) throws Exception {

        requestValidationStrategy.validateRequest(bankDetailsRequestDto, RequestType.BANK_DETAILS);

        uploadDocument(document,bankDetailsRequestDto);

        TenantBankDetails tenantBankDetails = MapperUtils.convertValue(bankDetailsRequestDto, TenantBankDetails.class);
        tenantBankDetailsRepository.save(tenantBankDetails);

        return MapperUtils.convertValue(tenantBankDetails, TenantBankResponseDto.class);
    }

    @Override
    public List<TenantBankResponseDto> getBankDetails(String userId) {

        List<TenantBankDetails> tenantBankDetailsList = tenantBankDetailsRepository.findByUserId(userId);

        List<TenantBankResponseDto> tenantBankResponseDtos  =new ArrayList<>();
        if(CollectionUtils.isNotEmpty(tenantBankDetailsList)){
            tenantBankDetailsList.forEach(tenantBankDetails -> {
                try {
                    tenantBankResponseDtos.add(MapperUtils.convertValue(tenantBankDetails,TenantBankResponseDto.class));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return tenantBankResponseDtos;
    }

    @Override
    public TenantBankResponseDto verifyBankDetails(VerifyBankDetails verifyBankDetails) throws Exception {
        TenantBankDetails tenantBankDetails = requestValidationStrategy.validateRequest(verifyBankDetails,RequestType.VERIFY_BANK_DETAILS);
        tenantBankDetails.setIsAccountVerified(true);
        tenantBankDetailsRepository.save(tenantBankDetails);
        return MapperUtils.convertValue(tenantBankDetails, TenantBankResponseDto.class);
    }

    @Override
    public EnachLinkingResponseDto enachLinking(EnachLinkingRequestDto enachLinkingRequestDto) throws Exception {
        requestValidationStrategy.validateRequest(enachLinkingRequestDto,RequestType.ENACH_LINKING);
        EnachLinkingEntity enachLinkingEntity = MapperUtils.convertValue(enachLinkingRequestDto,EnachLinkingEntity.class);
        enachLinkingRepository.save(enachLinkingEntity);
        return MapperUtils.convertValue(enachLinkingEntity,EnachLinkingResponseDto.class);
    }


    private Boolean uploadDocument(MultipartFile multipartFile,BankDetailsRequestDto requestDto){

        AtomicInteger imageNumber = new AtomicInteger(1);

        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String imageFolder = S3Util.getFolderString(String.valueOf(requestDto.getUserId()), BANK_IMAGE_FOLDER_NAME, String.valueOf(requestDto.getBaseBankId()));
        String imageFileKeyName = imageFolder + SLASH_SEPARATOR + imageNumber.getAndIncrement() +
                EXTENSION_SEPARATOR + fileExtension;

        File file = CsvUtils.convertMultipartFileToFile(multipartFile, imageFolder, imageFileKeyName);
        S3Util.uploadFileToS3(appProperties.getBucketName(), imageFileKeyName, file);
        requestDto.setAccountStatementLink(imageFileKeyName);

        CsvUtils.deleteFile(imageFolder, file);
        return true;
    }
}
