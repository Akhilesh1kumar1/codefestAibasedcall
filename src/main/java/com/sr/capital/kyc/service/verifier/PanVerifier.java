package com.sr.capital.kyc.service.verifier;


import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.ErrorDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanVerifiedDetails;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.external.adaptor.KarzaVerificationAdapter;
import com.sr.capital.kyc.external.response.verification.NameComparisonResponse;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import com.sr.capital.kyc.service.transformer.external.NameComparisonRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PanVerifier implements DetailsVerifier {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private NameComparisonRequestTransformer nameComparisonRequestTransformer;

    @Autowired
    private KarzaVerificationAdapter karzaVerificationAdapter;

    @Override
    @SuppressWarnings("unchecked")
    public VerifierResponse verify(DocOrchestratorRequest orchestratorRequest) throws ServiceEndpointNotFoundException {

        VerifierResponse verifierResponse = VerifierResponse.builder().build();
        PanDocDetails panDocDetails = ((KycDocDetails<PanDocDetails>) orchestratorRequest.getKycDocDetails()).getDetails();
        PanVerifiedDetails panVerifiedDetails = ((KycVerifiedDetails<PanVerifiedDetails>) orchestratorRequest.getKycVerifiedDetails()).getDetails();
        ErrorDetails error = orchestratorRequest.getKycVerifiedDetails().getError();

        if(error.isNotNull()) {
            verifierResponse.setIsVerified(false);
            verifierResponse.setComments(error.getMessage());
            return verifierResponse;
        }

        Boolean isVerified = panVerifiedDetails.getIdNumber().equalsIgnoreCase(panDocDetails.getIdNumber());

        if (isVerified) {
            isVerified = panVerifiedDetails.getNameOnCard().equalsIgnoreCase(panDocDetails.getNameOnCard());
            if (!isVerified) {
                NameComparisonResponse response = karzaVerificationAdapter.getNameComparisonScore(
                        nameComparisonRequestTransformer.transformRequest(panDocDetails.getNameOnCard(), panVerifiedDetails.getNameOnCard())
                );
                if (kycAppProperties.getAllowedNameScore() <= response.getResult().getMatchOutput().getNameMatch()) {
                    isVerified = Boolean.TRUE;
                }
            }
        }

        verifierResponse.setIsVerified(isVerified);
        return verifierResponse;
    }
}
