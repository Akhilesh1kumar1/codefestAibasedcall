package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.ExtractedGstResponse;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractedGstResponseConstructor implements ResponseConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<GstDocDetails> kycDocDetails = (KycDocDetails<GstDocDetails>) orchestratorRequest.getKycDocDetails();
        GstDocDetails gstDocDetails = kycDocDetails.getDetails();

        ExtractedGstResponse extractedGstResponse = ExtractedGstResponse.builder()
                .name(gstDocDetails.getLegalName())
                .tradeName(gstDocDetails.getTradeName())
                .address(gstDocDetails.getAddress())
                .gstin(gstDocDetails.getGstin())
                .constitutionOfBusiness(gstDocDetails.getConstitutionOfBusiness())
                .typeOfRegistration(gstDocDetails.getTypeOfRegistration())
                .panNumber(gstDocDetails.getPanNumber())
                .dateOfLiability(gstDocDetails.getDateOfLiability())
                .validUpTo(gstDocDetails.getValidUpTo())
                .isProvisional(gstDocDetails.isProvisional())
                .build();

        List<String> images = new ArrayList<>();
        List<String> imageIds = new ArrayList<>();
        images.add(orchestratorRequest.getFile1().getPreSignedUri());
        imageIds.add(orchestratorRequest.getFile1().getFileName());
        if(orchestratorRequest.hasFile2()) {
            images.add(orchestratorRequest.getFile2().getPreSignedUri());
            imageIds.add(orchestratorRequest.getFile2().getFileName());
        }

        FetchDocDetailsResponse<ExtractedGstResponse> docDetailsResponse = FetchDocDetailsResponse.<ExtractedGstResponse>builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType())
                .images(images)
                .imageIds(imageIds)
                .status(kycDocDetails.getStatus())
                .details(extractedGstResponse)
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
