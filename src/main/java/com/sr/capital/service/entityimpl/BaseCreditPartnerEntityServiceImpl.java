package com.sr.capital.service.entityimpl;

import com.sr.capital.dto.request.CreateBaseCreditPartnerDto;
import com.sr.capital.dto.request.UpdateBaseCreditPartnerDto;
import com.sr.capital.dto.response.BaseCreditPartnerResponseDto;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.repository.primary.BaseCreditPartnerRepository;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.NO_RECORD_FOUND_WITH_GIVEN_DETAILS;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseCreditPartnerEntityServiceImpl {

    final BaseCreditPartnerRepository baseCreditPartnerRepository;
    LoggerUtil loggerUtil = LoggerUtil.getLogger(BaseCreditPartnerEntityServiceImpl.class);

    final RedissonClient redissonClient;

    public boolean createBaseCreditPartner(CreateBaseCreditPartnerDto createBaseCreditPartnerDto) throws CustomException {
        validateBaseCreditPartner(createBaseCreditPartnerDto);
        BaseCreditPartner baseCreditPartner =BaseCreditPartner.mapBaseCreditPartnerFromDto(createBaseCreditPartnerDto);
        baseCreditPartnerRepository.save(baseCreditPartner);
        saveInCache(baseCreditPartner);
        return true;
    }

    private void validateBaseCreditPartner(CreateBaseCreditPartnerDto createBaseCreditPartnerDto) throws CustomException {
        BaseCreditPartner baseCreditPartner = baseCreditPartnerRepository.findByCreditPartnerName(createBaseCreditPartnerDto.getCreditPartnerName());
        if(baseCreditPartner!=null){
            throw new CustomException("Credit partner already exist",HttpStatus.BAD_REQUEST);
        }
    }

    public boolean updateBaseCreditPartner(UpdateBaseCreditPartnerDto updateBaseCreditPartnerDto) throws CustomException {

        Optional<BaseCreditPartner> optionalBaseCreditPartner = baseCreditPartnerRepository.findById(updateBaseCreditPartnerDto.getId());
        if(optionalBaseCreditPartner.isPresent()){
            BaseCreditPartner baseCreditPartner =optionalBaseCreditPartner.get();
            baseCreditPartner.setIsEnabled(updateBaseCreditPartnerDto.getEnabled());
            baseCreditPartner.setCreditPartnerName(updateBaseCreditPartnerDto.getCreditPartnerName());
            baseCreditPartner.setImageLink(updateBaseCreditPartnerDto.getImageLink());
            baseCreditPartner.setDescription(updateBaseCreditPartnerDto.getDescription());
            baseCreditPartnerRepository.save(baseCreditPartner);
            saveInCache(baseCreditPartner);
        }else{
            throw new CustomException(NO_RECORD_FOUND_WITH_GIVEN_DETAILS, HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    public List<BaseCreditPartnerResponseDto> getAllCreditPartner() {
        RMapCache<String, BaseCreditPartner> channelInfo = redissonClient.getMapCache(Constants.RedisKeys.BASE_CREDIT_PARTNER);
        return creditPartnerDtoFromBaseCreditPartnerList(new ArrayList<>(channelInfo.values()));
    }

    public boolean syncAllBaseCreditPartnerToCache() {
        List<BaseCreditPartner> baseCreditPartnerList = (List<BaseCreditPartner>) baseCreditPartnerRepository.findAll();
        RMapCache<String, BaseCreditPartner> baseCreditPartnerRMapCache = redissonClient.getMapCache(Constants.RedisKeys.BASE_CREDIT_PARTNER);
        baseCreditPartnerRMapCache.clear();
        baseCreditPartnerRMapCache.putAll(baseCreditPartnerList.stream().collect(Collectors.toMap(BaseCreditPartner::getCreditPartnerName, Function.identity())));
        return true;
    }

    private void saveInCache(BaseCreditPartner baseCreditPartner) {
        RMapCache<String, BaseCreditPartner> baseCreditPartnerCache = redissonClient.getMapCache(Constants.RedisKeys.BASE_CREDIT_PARTNER);
        baseCreditPartnerCache.put(baseCreditPartner.getCreditPartnerName(), baseCreditPartner);
    }

    private List<BaseCreditPartnerResponseDto> creditPartnerDtoFromBaseCreditPartnerList(List<BaseCreditPartner> baseCreditPartnerList) {
        return MapperUtils.mapList(baseCreditPartnerList, BaseCreditPartnerResponseDto.class);
    }

}
