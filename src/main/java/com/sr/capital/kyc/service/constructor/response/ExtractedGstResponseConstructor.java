package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.ExtractedGstResponse;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.external.response.extraction.GstExtractionResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtractedGstResponseConstructor implements ResponseConstructor {

    final AES256 aes256;
    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<GstDocDetails> kycDocDetails = (KycDocDetails<GstDocDetails>) orchestratorRequest.getKycDocDetails();
        GstDocDetails gstDocDetails = kycDocDetails.getDetails();
        GstDocDetails gstDocDetails1 = null;
        try {
            gstDocDetails1 = MapperUtils.convertValue( orchestratorRequest.getDocDetails(), GstDocDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GstExtractionResponse gstExtractionResponse = (GstExtractionResponse) orchestratorRequest.getKarzaBaseResponse();
        ExtractedGstResponse extractedGstResponse = ExtractedGstResponse.builder().build();
        extractedGstResponse.setGstUserDetails(new ArrayList<>());
        if(gstDocDetails1!=null) {
           extractedGstResponse.setGstin(gstDocDetails1.getGstin());
           extractedGstResponse.setUsername(gstDocDetails1.getUsername());
           extractedGstResponse.setRequestId(gstExtractionResponse.getRequestId());
        }

        gstDocDetails.getGstDetails().forEach(gstUserDetails -> {
            extractedGstResponse.getGstUserDetails().add(ExtractedGstResponse.GstUserDetails.builder().gstin(aes256.decrypt(gstUserDetails.getGstin())).username(aes256.decrypt(gstUserDetails.getUsername())).refId(gstUserDetails.getRefId()).build());
        });


        FetchDocDetailsResponse<ExtractedGstResponse> docDetailsResponse = FetchDocDetailsResponse.<ExtractedGstResponse>builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType())
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
