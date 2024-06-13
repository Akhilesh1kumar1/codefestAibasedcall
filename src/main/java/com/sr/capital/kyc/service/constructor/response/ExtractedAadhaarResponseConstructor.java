package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.ExtractedAadhaarResponse;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractedAadhaarResponseConstructor implements ResponseConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<AadhaarDocDetails> kycDocDetails = (KycDocDetails<AadhaarDocDetails>) orchestratorRequest.getKycDocDetails();
        AadhaarDocDetails aadhaarDocDetails = kycDocDetails.getDetails();

        ExtractedAadhaarResponse extractedAadhaarResponse = ExtractedAadhaarResponse.builder()
                .idNumber(aadhaarDocDetails.getIdNumber())
                .name(aadhaarDocDetails.getNameOnCard())
                .fathersName(aadhaarDocDetails.getFathersName())
                .dateOfBirth(aadhaarDocDetails.getDateOfBirth())
                .yearOfBirth(aadhaarDocDetails.getYearOfBirth())
                .gender(aadhaarDocDetails.getGender())
                .houseNumber(aadhaarDocDetails.getHouseNumber())
                .streetAddress(aadhaarDocDetails.getStreetAddress())
                .address(aadhaarDocDetails.getAddress())
                .district(aadhaarDocDetails.getDistrict())
                .state(aadhaarDocDetails.getState())
                .pincode(aadhaarDocDetails.getPincode())
                .build();

        List<String> images = new ArrayList<>();
        List<String> imageIds = new ArrayList<>();
        images.add(orchestratorRequest.getFile1().getPreSignedUri());
        imageIds.add(orchestratorRequest.getFile1().getFileName());
        if(orchestratorRequest.hasFile2()) {
            images.add(orchestratorRequest.getFile2().getPreSignedUri());
            imageIds.add(orchestratorRequest.getFile2().getFileName());
        }

        FetchDocDetailsResponse<ExtractedAadhaarResponse> docDetailsResponse = FetchDocDetailsResponse.<ExtractedAadhaarResponse>builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType())
                .images(images)
                .imageIds(imageIds)
                .status(kycDocDetails.getStatus())
                .details(extractedAadhaarResponse)
                .build();


        GenericResponse<T> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData((T) docDetailsResponse);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );

    }
}
