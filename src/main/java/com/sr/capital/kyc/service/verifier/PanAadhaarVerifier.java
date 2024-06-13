package com.sr.capital.kyc.service.verifier;


import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.ErrorDetails;
import com.sr.capital.entity.mongo.kyc.child.PanAadhaarCrossVerifiedDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import org.springframework.stereotype.Service;

@Service
public class PanAadhaarVerifier implements DetailsVerifier {

    @Override
    @SuppressWarnings("unchecked")
    public VerifierResponse verify(DocOrchestratorRequest orchestratorRequest) {

        VerifierResponse verifierResponse = VerifierResponse.builder().build();
        PanAadhaarCrossVerifiedDetails panAadhaarCrossVerifiedDetails = ((KycVerifiedDetails<PanAadhaarCrossVerifiedDetails>) orchestratorRequest.getKycVerifiedDetails()).getDetails();
        ErrorDetails error = orchestratorRequest.getKycVerifiedDetails().getError();

        if(error.isNotNull()) {
            verifierResponse.setIsVerified(false);
            verifierResponse.setComments(error.getMessage());
            return verifierResponse;
        }

        verifierResponse.setIsVerified(panAadhaarCrossVerifiedDetails.getIsLinked());
        return verifierResponse;
    }
}
