package com.sr.capital.kyc.service.verifier;


import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.ErrorDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstVerifiedDetails;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.external.adaptor.IdfyVerificationAdapter;
import com.sr.capital.kyc.external.response.verification.NameComparisonResponse;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import com.sr.capital.kyc.service.transformer.external.NameComparisonRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GstVerifier implements DetailsVerifier {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private NameComparisonRequestTransformer nameComparisonRequestTransformer;

    @Autowired
    private IdfyVerificationAdapter idfyVerificationAdapter;

    @Override
    @SuppressWarnings("unchecked")
    public VerifierResponse verify(DocOrchestratorRequest orchestratorRequest) throws ServiceEndpointNotFoundException {

        VerifierResponse verifierResponse = VerifierResponse.builder().build();
        GstDocDetails gstDocDetails = ((KycDocDetails<GstDocDetails>) orchestratorRequest.getKycDocDetails()).getDetails();
        GstVerifiedDetails gstVerifiedDetails = ((KycVerifiedDetails<GstVerifiedDetails>) orchestratorRequest.getKycVerifiedDetails()).getDetails();
        ErrorDetails error = orchestratorRequest.getKycVerifiedDetails().getError();

        if(error.isNotNull()) {
            verifierResponse.setIsVerified(false);
            verifierResponse.setComments(error.getMessage());
            return verifierResponse;
        }

        Boolean isVerified = gstVerifiedDetails.getGstin().equalsIgnoreCase(gstDocDetails.getGstin());

        if (isVerified) {
            isVerified = gstVerifiedDetails.getTradeName().equalsIgnoreCase(gstDocDetails.getTradeName());
            if (!isVerified) {
                NameComparisonResponse response = idfyVerificationAdapter.getNameComparisonScore(
                        nameComparisonRequestTransformer.transformRequest(gstDocDetails.getTradeName(), gstVerifiedDetails.getTradeName())
                );
                if (kycAppProperties.getAllowedNameScore() <= response.getResult().getMatchOutput().getNameMatch()) {
                    isVerified = Boolean.TRUE;
                }
            }
        }

        if (isVerified) {
            isVerified = gstVerifiedDetails.getLegalName().equalsIgnoreCase(gstDocDetails.getLegalName());
            if (!isVerified) {
                NameComparisonResponse response = idfyVerificationAdapter.getNameComparisonScore(
                        nameComparisonRequestTransformer.transformRequest(gstDocDetails.getLegalName(), gstVerifiedDetails.getLegalName())
                );
                if (kycAppProperties.getAllowedNameScore() <= response.getResult().getMatchOutput().getNameMatch()) {
                    isVerified = Boolean.TRUE;
                }
            }
        }
        return VerifierResponse.builder().isVerified(isVerified).build();
    }
}
