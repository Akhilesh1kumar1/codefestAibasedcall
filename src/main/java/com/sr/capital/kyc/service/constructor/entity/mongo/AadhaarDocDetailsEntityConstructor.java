package com.sr.capital.kyc.service.constructor.entity.mongo;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AadhaarDocDetailsEntityConstructor implements EntityConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) {

       /* AadhaarExtractionResponse response = (AadhaarExtractionResponse) request.getIdfyGenericResponse();
        AadhaarExtractionResponseData responseData = response.getResult();

        List<String> images = new ArrayList<>();
        images.add(request.getFile1().getFileName());
        if(request.hasFile2()){
            images.add(request.getFile2().getFileName());
        }

        if (ObjectUtils.isNotEmpty(entity)) {

            KycDocDetails<AadhaarDocDetails> kycDocDetails = (KycDocDetails<AadhaarDocDetails>) request.getKycDocDetails();
            kycDocDetails.setDetails(getAadhaarExtractionResponse(responseData.getExtractionOutput()));
            kycDocDetails.setImages(images);
            return (T) kycDocDetails;

        } else {
            return (T) KycDocDetails.<AadhaarDocDetails>builder()
                .tenantId(request.getSrCompanyId())
                .images(images)
                .docType(DocType.AADHAAR)
                .details(getAadhaarExtractionResponse(responseData.getExtractionOutput()))
                .build();
        }*/
       return null;
    }

    /*private AadhaarDocDetails getAadhaarExtractionResponse(AadhaarExtractionResponseData.ExtractionOutput extractionOutput) {
        return AadhaarDocDetails.builder()
            .address(extractionOutput.getAddress())
            .dateOfBirth(extractionOutput.getDateOfBirth())
            .district(extractionOutput.getDistrict())
            .fathersName(extractionOutput.getFathersName())
            .gender(extractionOutput.getGender())
            .houseNumber(extractionOutput.getHouseNumber())
            .idNumber(extractionOutput.getIdNumber())
            .isScanned(extractionOutput.getIsScanned())
            .nameOnCard(extractionOutput.getNameOnCard())
            .pincode(extractionOutput.getPincode())
            .state(extractionOutput.getState())
            .streetAddress(extractionOutput.getStreetAddress())
            .yearOfBirth(extractionOutput.getYearOfBirth())
            .build();
    }*/

}
