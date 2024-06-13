package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.ExtractedBankResponse;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractedBankResponseConstructor implements ResponseConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<BankDocDetails> kycDocDetails = (KycDocDetails<BankDocDetails>) orchestratorRequest.getKycDocDetails();
        BankDocDetails bankDocDetails = kycDocDetails.getDetails();

        ExtractedBankResponse extractedBankResponse = ExtractedBankResponse.builder()
                .name(bankDocDetails.getAccountName())
                .accountNo(bankDocDetails.getAccountNo())
                .micrCode(bankDocDetails.getMicrCode())
                .micrChequeNumber(bankDocDetails.getMicrChequeNumber())
                .dateOfIssue(bankDocDetails.getDateOfIssue())
                .bankName(bankDocDetails.getBankName())
                .ifscCode(bankDocDetails.getIfscCode())
                .bankAddress(bankDocDetails.getBankAddress())
                .build();

        List<String> images = new ArrayList<>();
        List<String> imageIds = new ArrayList<>();
        images.add(orchestratorRequest.getFile1().getPreSignedUri());
        imageIds.add(orchestratorRequest.getFile1().getFileName());
        if(orchestratorRequest.hasFile2()) {
            images.add(orchestratorRequest.getFile2().getPreSignedUri());
            imageIds.add(orchestratorRequest.getFile2().getFileName());
        }

        FetchDocDetailsResponse<ExtractedBankResponse> docDetailsResponse = FetchDocDetailsResponse.<ExtractedBankResponse>builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType())
                .images(images)
                .imageIds(imageIds)
                .status(kycDocDetails.getStatus())
                .details(extractedBankResponse)
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
