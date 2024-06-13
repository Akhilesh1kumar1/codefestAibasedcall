package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.request.verification.GstVerificationRequest;
import com.sr.capital.kyc.external.request.verification.data.GstVerificationData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

@Service
public class GstVerificationRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdfyBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        KycDocDetails<GstDocDetails> kycDocDetails = (KycDocDetails<GstDocDetails>) request.getKycDocDetails();
        GstDocDetails gstDocDetails = kycDocDetails.getDetails();

        GstVerificationData gstVerificationData = GstVerificationData.builder()
                .gstin(gstDocDetails.getGstin())
                .build();

        return (T) GstVerificationRequest.builder()
                .taskId(request.getTask().getId().toString())
                .groupId(request.getTask().getGroupId().toString())
                .data(gstVerificationData)
                .build();
    }
}
