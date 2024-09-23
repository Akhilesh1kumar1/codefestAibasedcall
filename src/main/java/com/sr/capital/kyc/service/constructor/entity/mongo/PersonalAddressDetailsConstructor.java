package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PersonalAddressDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.PersonalAddressDetailsRequestDto;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalAddressDetailsConstructor implements EntityConstructor {

    LoggerUtil loggerUtil = LoggerUtil.getLogger(ItrDocDetailsEntityConstructor.class);

    final AES256 aes256;


    @Override
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException {
        PersonalAddressDetailsRequestDto personalAddressDetailsRequest =null;
        try {
            personalAddressDetailsRequest = MapperUtils.convertValue(request.getDocDetails(),PersonalAddressDetailsRequestDto.class);
        }catch (Exception ex){
            loggerUtil.error("error in doc parsing "+ex.getMessage());
        }

        List<PersonalAddressDetails.Address> addressList =new ArrayList<>();
        personalAddressDetailsRequest.getAddress().stream().forEach(personalAddressDetailsRequestDto-> {
                    PersonalAddressDetails.Address address = PersonalAddressDetails.Address.builder().address(aes256.encrypt(personalAddressDetailsRequestDto.getAddress()))
                            .pincode(aes256.encrypt(personalAddressDetailsRequestDto.getPincode())).city(aes256.encrypt(personalAddressDetailsRequestDto.getCity())).addressType(personalAddressDetailsRequestDto.getAddressType())

                            .state(aes256.encrypt(personalAddressDetailsRequestDto.getState())).build();
                    addressList.add(address);
                });
        PersonalAddressDetails personalAddressDetails = PersonalAddressDetails.builder().address(addressList).metaData(personalAddressDetailsRequest.getMetaData()).build();

        KycDocDetails<PersonalAddressDetails> kycDocDetails = (KycDocDetails<PersonalAddressDetails>) request.getKycDocDetails();


        if(kycDocDetails==null){
            kycDocDetails= KycDocDetails.<PersonalAddressDetails>builder()
                    .srCompanyId(RequestData.getTenantId())
                    .docType(DocType.PERSONAL_ADDRESS)
                    .details(personalAddressDetails)
                    .kycType(request.getKycType())
                    .build();
        }else{
            kycDocDetails.setDetails(personalAddressDetails);
            kycDocDetails.setLastModifiedAt(LocalDateTime.now());
            kycDocDetails.setKycType(request.getKycType());
            if(RequestData.getUserId()!=null){
                kycDocDetails.setLastModifiedBy(String.valueOf(RequestData.getUserId()));
            }
        }
        return (T) kycDocDetails;
    }
}
