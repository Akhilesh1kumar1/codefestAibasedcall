package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.request.verification.PanGstCrossVerificationRequest;
import com.sr.capital.kyc.external.request.verification.data.PanGstCrossVerificationData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

@Service
public class PanGstCrossVerificationRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdfyBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        KycDocDetails<PanDocDetails> kycDocDetails = (KycDocDetails<PanDocDetails>) request.getKycDocDetails();
        PanDocDetails panDocDetails = kycDocDetails.getDetails();

        PanGstCrossVerificationData panGstCrossVerificationData = PanGstCrossVerificationData.builder()
                .panNumber(panDocDetails.getIdNumber())
                .build();

        return (T) PanGstCrossVerificationRequest.builder()
                .taskId(request.getTask().getId().toString())
                .groupId(request.getTask().getGroupId().toString())
                .data(panGstCrossVerificationData)
                .build();
    }
}
