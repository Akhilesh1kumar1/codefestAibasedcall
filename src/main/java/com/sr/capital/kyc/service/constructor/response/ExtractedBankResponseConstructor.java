package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.ExtractedBankResponse;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtractedBankResponseConstructor implements ResponseConstructor {

    final AES256 aes256;
    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<List<BankDocDetails>> kycDocDetails = (KycDocDetails<List<BankDocDetails>>) orchestratorRequest.getKycDocDetails();
       List<ExtractedBankResponse> extractedBankResponses =new ArrayList<>();
        kycDocDetails.getDetails().forEach(bankDocDetails -> {

            extractedBankResponses.add( ExtractedBankResponse.builder()
                    .name(aes256.decrypt(bankDocDetails.getAccountName()))
                    .accountNo(aes256.decrypt(bankDocDetails.getAccountNo()))
                    .micrCode(bankDocDetails.getMicrCode())
                    .micrChequeNumber(bankDocDetails.getMicrChequeNumber())
                    .dateOfIssue(bankDocDetails.getDateOfIssue())
                    .bankName(bankDocDetails.getBankName())
                    .ifscCode(bankDocDetails.getIfscCode())
                    .bankAddress(aes256.decrypt(bankDocDetails.getBankAddress()))
                    .build());
        });



        List<String> images = new ArrayList<>();
        List<String> imageIds = new ArrayList<>();
        if(orchestratorRequest.getFile1()!=null) {
            images.add(orchestratorRequest.getFile1().getPreSignedUri());
            imageIds.add(orchestratorRequest.getFile1().getFileName());
            if (orchestratorRequest.hasFile2()) {
                images.add(orchestratorRequest.getFile2().getPreSignedUri());
                imageIds.add(orchestratorRequest.getFile2().getFileName());
            }
        }
        FetchDocDetailsResponse<List<ExtractedBankResponse>> docDetailsResponse = FetchDocDetailsResponse.<List<ExtractedBankResponse>>builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType())
                .images(images)
                .imageIds(imageIds)
                .status(kycDocDetails.getStatus())
                .details(extractedBankResponses)
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
