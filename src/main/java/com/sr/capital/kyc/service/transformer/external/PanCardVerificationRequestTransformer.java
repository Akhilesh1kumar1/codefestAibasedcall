package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.request.verification.PanCardVerificationRequest;
import com.sr.capital.kyc.external.request.verification.data.PanCardVerificationData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

@Service
public class PanCardVerificationRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdfyBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        KycDocDetails<PanDocDetails> kycDocDetails = (KycDocDetails<PanDocDetails>) request.getKycDocDetails();
        PanDocDetails panDocDetails = kycDocDetails.getDetails();

        PanCardVerificationData panCardVerificationData = PanCardVerificationData.builder()
                .idNumber(panDocDetails.getIdNumber())
                .build();

        return (T) PanCardVerificationRequest.builder()
                .taskId(request.getTask().getId().toString())
                .groupId(request.getTask().getGroupId().toString())
                .data(panCardVerificationData)
                .build();
    }
}
