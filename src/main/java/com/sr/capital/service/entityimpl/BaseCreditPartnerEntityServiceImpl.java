package com.sr.capital.service.entityimpl;

import com.sr.capital.dto.request.CreateBaseCreditPartnerDto;
import com.sr.capital.dto.request.UpdateBaseCreditPartnerDto;
import com.sr.capital.entity.BaseCreditPartner;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.repository.BaseCreditPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.NO_RECORD_FOUND_WITH_GIVEN_DETAILS;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseCreditPartnerEntityServiceImpl {

    final BaseCreditPartnerRepository baseCreditPartnerRepository;


    public boolean createBaseCreditPartner(CreateBaseCreditPartnerDto createBaseCreditPartnerDto){
         baseCreditPartnerRepository.save(BaseCreditPartner.mapBaseCreditPartnerFromDto(createBaseCreditPartnerDto));
         return true;
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
        }else{
            throw new CustomException(NO_RECORD_FOUND_WITH_GIVEN_DETAILS, HttpStatus.BAD_REQUEST);
        }
        return true;
    }

}
