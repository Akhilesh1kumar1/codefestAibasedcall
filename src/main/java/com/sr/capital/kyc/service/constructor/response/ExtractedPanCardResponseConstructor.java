package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.ExtractedPanResponse;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractedPanCardResponseConstructor implements ResponseConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<PanDocDetails> kycDocDetails = (KycDocDetails<PanDocDetails>) orchestratorRequest.getKycDocDetails();
        PanDocDetails panDocDetails = kycDocDetails.getDetails();

        ExtractedPanResponse extractedPanResponse = ExtractedPanResponse.builder()
                .name(panDocDetails.getNameOnCard())
                .fathersName(panDocDetails.getFathersName())
                .dateOfBirth(panDocDetails.getDateOfBirth())
                .age(panDocDetails.getAge())
                .minor(panDocDetails.isMinor())
                .idNumber(panDocDetails.getIdNumber())
                .panType(panDocDetails.getPanType())
                .dateOfIssue(panDocDetails.getDateOfIssue())
                .build();

        List<String> images = new ArrayList<>();
        List<String> imageIds = new ArrayList<>();
        images.add(orchestratorRequest.getFile1().getPreSignedUri());
        imageIds.add(orchestratorRequest.getFile1().getFileName());
        if(orchestratorRequest.hasFile2()) {
            images.add(orchestratorRequest.getFile2().getPreSignedUri());
            imageIds.add(orchestratorRequest.getFile2().getFileName());
        }

        FetchDocDetailsResponse<ExtractedPanResponse> docDetailsResponse = FetchDocDetailsResponse.<ExtractedPanResponse>builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType())
                .images(images)
                .imageIds(imageIds)
                .status(kycDocDetails.getStatus())
                .details(extractedPanResponse)
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
