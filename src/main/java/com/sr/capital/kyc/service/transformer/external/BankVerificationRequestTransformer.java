package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.verification.BankVerificationRequest;
import com.sr.capital.kyc.external.request.verification.data.BankVerificationData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

@Service
public class BankVerificationRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        KycDocDetails<BankDocDetails> kycDocDetails = (KycDocDetails<BankDocDetails>) request.getKycDocDetails();
        BankDocDetails bankDocDetails = kycDocDetails.getDetails();

        BankVerificationData bankVerificationData = BankVerificationData.builder()
                .bankAccountNo(bankDocDetails.getAccountNo())
                .bankIfscCode(bankDocDetails.getIfscCode())
                .build();

        return (T) BankVerificationRequest.builder()
                .taskId(request.getTask().getId().toString())
                .groupId(request.getTask().getGroupId().toString())
                .data(bankVerificationData)
                .build();
    }
}
