package com.sr.capital.service.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.AdharUpdateRequestDto;
import com.sr.capital.dto.response.AdharUpdateResponse;
import com.sr.capital.entity.AdharDetails;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.repository.AdharRepository;
import com.sr.capital.service.AdharService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sr.capital.helpers.constants.Constants.ServiceConstants.ADHAR_IMAGE_FOLDER_NAME;

@Service
@AllArgsConstructor
public class AdharServiceImpl implements AdharService {

    final RequestValidationStrategy requestValidationStrategy;
    final AdharRepository adharRepository;
    final AppProperties appProperties;
    @Override
    public AdharUpdateResponse addAdharDetails(AdharUpdateRequestDto adharUpdateRequestDto,List<MultipartFile> multipartFileList) throws Exception {
        requestValidationStrategy.validateRequest(adharUpdateRequestDto, RequestType.ADHAR);
        AdharDetails adharDetails = MapperUtils.convertValue(adharUpdateRequestDto,AdharDetails.class);
        adharDetails = adharRepository.save(adharDetails);
        if(CollectionUtils.isNotEmpty(multipartFileList)){
            updateImages(multipartFileList,adharUpdateRequestDto,adharDetails);
        }
        return MapperUtils.convertValue(adharDetails,AdharUpdateResponse.class);
    }

    @Override
    public AdharDetails getAdharDetailsById(Long adharId) {
        AdharDetails adharDetails =null;
        Optional<AdharDetails> adharDetailsOptional = adharRepository.findById(adharId);
        if(adharDetailsOptional.isPresent()){
            adharDetails= adharDetailsOptional.get();
        }
        return adharDetails;
    }


    public boolean updateImages(List<MultipartFile> multipartFileList,AdharUpdateRequestDto adharUpdateRequestDto,AdharDetails adharDetails){
        AtomicInteger imageNumber = new AtomicInteger();
        multipartFileList.forEach(multipartFile -> {
            String imageUrl = S3Util.uploadDocument(multipartFile,appProperties.getBucketName(),String.valueOf(adharUpdateRequestDto.getUserId()),ADHAR_IMAGE_FOLDER_NAME,String.valueOf(adharDetails.getId()),imageNumber);

            if(imageNumber.get()==0){
                adharDetails.setAdharImageLink1(imageUrl);
            }else
                adharDetails.setAdharImageLink2(imageUrl);

            imageNumber.incrementAndGet();

        });
         adharRepository.save(adharDetails);
        return true;
    }


}
