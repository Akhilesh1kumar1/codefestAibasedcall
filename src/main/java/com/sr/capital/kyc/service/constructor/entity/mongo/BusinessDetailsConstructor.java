package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BusinessAddressDetails;
import com.sr.capital.entity.mongo.kyc.child.ItrDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.BusinessDetailsRequestDto;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.response.extraction.ItrExtractionResponseData;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessDetailsConstructor implements EntityConstructor {

    LoggerUtil loggerUtil = LoggerUtil.getLogger(ItrDocDetailsEntityConstructor.class);

    final AES256 aes256;


    @Override
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException {

        BusinessDetailsRequestDto businessDetailsRequestDto =null;
        try {
            businessDetailsRequestDto = MapperUtils.convertValue(request.getDocDetails(),BusinessDetailsRequestDto.class);
        }catch (Exception ex){
            loggerUtil.error("error in doc parsing "+ex.getMessage());
        }

        List<BusinessAddressDetails.BusinessPartnerInfo > partnerInfoList;
        if(CollectionUtils.isNotEmpty(businessDetailsRequestDto.getBusinessPartnerInfo())){
            partnerInfoList =new ArrayList<>();
            businessDetailsRequestDto.getBusinessPartnerInfo().forEach(partnerInfoDto->{
                BusinessAddressDetails.BusinessPartnerInfo partnerInfo =BusinessAddressDetails.BusinessPartnerInfo.builder()
                        .dob(aes256.encrypt(partnerInfoDto.getDob()))
                        .address(aes256.encrypt(partnerInfoDto.getAddress()))
                        .name(aes256.encrypt(partnerInfoDto.getName()))
                        .gender(partnerInfoDto.getGender()).mobileNumber(aes256.encrypt(partnerInfoDto.getMobileNumber())).pincode(aes256.encrypt(partnerInfoDto.getPincode()))
                        .panNumber(aes256.encrypt(partnerInfoDto.getPanNumber())).businessPartnerHolding(aes256.encrypt(partnerInfoDto.getBusinessPartnerHolding())).build();
                partnerInfoList.add(partnerInfo);
            });
        } else {
            partnerInfoList = null;
        }

        BusinessAddressDetails  businessAddressDetails = BusinessAddressDetails.builder().pincode(aes256.encrypt(businessDetailsRequestDto.getPincode())).businessName(businessDetailsRequestDto.getBusinessName()).
                city(aes256.encrypt(businessDetailsRequestDto.getCity())).state(aes256.encrypt(businessDetailsRequestDto.getState())).address1(aes256.encrypt(businessDetailsRequestDto.getAddress1())).address2(aes256.encrypt(businessDetailsRequestDto.getAddress2())).
                metaData(businessDetailsRequestDto.getMetaData()).
                sectorType(businessDetailsRequestDto.getSectorType()).
                businessType(businessDetailsRequestDto.getBusinessType()).industryType(businessDetailsRequestDto.getIndustryType()).
                businessPanNumber(aes256.encrypt(businessDetailsRequestDto.getBusinessPanNumber())).businessOwnerShipStatus(businessDetailsRequestDto.getBusinessOwnerShipStatus()).gstRegistered(businessDetailsRequestDto.getGstRegistered()).noOfDirector(businessDetailsRequestDto.getNoOfDirector())
                .businessPartnerInfo(partnerInfoList).build();
        KycDocDetails<BusinessAddressDetails> kycDocDetails = (KycDocDetails<BusinessAddressDetails>) request.getKycDocDetails();


        if(kycDocDetails==null){
            kycDocDetails= KycDocDetails.<BusinessAddressDetails>builder()
                    .srCompanyId(RequestData.getTenantId())
                    .docType(DocType.BUSINESS_ADDRESS)
                    .details(businessAddressDetails)
                    .kycType(request.getKycType())
                    .build();
        }else{
            kycDocDetails.setKycType(request.getKycType());
            kycDocDetails.setDetails(businessAddressDetails);
        }
        return (T) kycDocDetails;
    }
}
