package com.sr.capital.service.impl;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.PAN_NOT_FOUND;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.PAN_IMAGE_FOLDER_NAME;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.PANUpdateRequestDto;
import com.sr.capital.dto.response.PANUpdateResponse;
import com.sr.capital.entity.PANDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.repository.PANRepository;
import com.sr.capital.service.PANService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PANServiceImpl implements PANService {

    final RequestValidationStrategy requestValidationStrategy;
    final PANRepository panRepository;
    final AppProperties appProperties;
    final LoggerUtil loggerUtil = LoggerUtil.getLogger(PANServiceImpl.class);

    @Override
    public PANUpdateResponse createPanDetails(PANUpdateRequestDto panUpdateRequestDto,
            List<MultipartFile> multipartFileList) throws Exception {

        Long userId = RequestData.getUserId();
        requestValidationStrategy.validateRequest(panUpdateRequestDto.getPanNumber(), RequestType.PAN);

        if (panRepository.existsByUserId(userId)) {
            throw new CustomException("PAN details already exists", HttpStatus.BAD_REQUEST);
        }

        panUpdateRequestDto.setUserId(userId);
        PANDetails panDetails = MapperUtils.mapClass(panUpdateRequestDto, PANDetails.class);
        panDetails = panRepository.save(panDetails);
        if (CollectionUtils.isNotEmpty(multipartFileList)) {
            updateImages(multipartFileList, panUpdateRequestDto, panDetails);
        }
        return MapperUtils.convertValue(panDetails, PANUpdateResponse.class);
    }

    @Override
    public PANDetails readPanDetails(Long userId) {
        PANDetails panDetails = null;
        Optional<PANDetails> panDetailsOptional = panRepository.findByUserId(userId);
        if (panDetailsOptional.isPresent()) {
            panDetails = panDetailsOptional.get();
        }
        return panDetails;
    }

    @Override
    public PANUpdateResponse updatePanDetails(PANUpdateRequestDto panUpdateRequestDto,
            List<MultipartFile> multipartFileList) throws Exception {

        Optional<PANDetails> pOptional = panRepository.findById(panUpdateRequestDto.getId());
        PANDetails panDetails = null;

        if (pOptional.isPresent()) {
            panDetails = pOptional.get();
            MapperUtils.mapClass(panUpdateRequestDto, panDetails);
            panDetails = panRepository.save(panDetails);
            if (CollectionUtils.isNotEmpty(multipartFileList)) {
                updateImages(multipartFileList, panUpdateRequestDto, panDetails);
            }
        } else {
            throw new CustomException(PAN_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        return MapperUtils.mapClass(panDetails, PANUpdateResponse.class);
    }

    private boolean updateImages(List<MultipartFile> multipartFileList, PANUpdateRequestDto panUpdateRequestDto,
            PANDetails panDetails) {

        AtomicInteger imageNumber = new AtomicInteger();
        multipartFileList.forEach(multipartFile -> {
            String imageUrl = S3Util.uploadDocument(multipartFile, appProperties.getBucketName(),
                    String.valueOf(panUpdateRequestDto.getUserId()), PAN_IMAGE_FOLDER_NAME,
                    String.valueOf(panDetails.getId()), imageNumber);

            if (imageNumber.get() == 1) {
                panDetails.setPanImageLink1(imageUrl);
            } else
                panDetails.setPanImageLink2(imageUrl);
        });

        panRepository.save(panDetails);
        return true;
    }

}
