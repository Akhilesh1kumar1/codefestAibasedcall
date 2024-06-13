package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.request.verification.AadhaarVerificationRequest;
import com.sr.capital.kyc.external.request.verification.data.AadhaarVerificationData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

@Service
public class AadhaarVerificationRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdfyBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        KycDocDetails<AadhaarDocDetails> kycDocDetails = (KycDocDetails<AadhaarDocDetails>) request.getKycDocDetails();
        AadhaarDocDetails aadhaarDocDetails = kycDocDetails.getDetails();

        AadhaarVerificationData aadhaarVerificationData = AadhaarVerificationData.builder()
                .aadhaarNumber(aadhaarDocDetails.getIdNumber())
                .build();

        return (T) AadhaarVerificationRequest.builder()
                .taskId(request.getTask().getId().toString())
                .groupId(request.getTask().getGroupId().toString())
                .data(aadhaarVerificationData)
                .build();
    }
}
