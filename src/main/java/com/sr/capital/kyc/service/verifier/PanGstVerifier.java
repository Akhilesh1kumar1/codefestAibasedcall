package com.sr.capital.kyc.service.verifier;


import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.ErrorDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanGstCrossVerifiedDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PanGstVerifier implements DetailsVerifier {

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Override
    @SuppressWarnings("unchecked")
    public VerifierResponse verify(DocOrchestratorRequest orchestratorRequest) {

        VerifierResponse verifierResponse = VerifierResponse.builder().isVerified(false).build();
        PanGstCrossVerifiedDetails panGstCrossVerifiedDetails = ((KycVerifiedDetails<PanGstCrossVerifiedDetails>) orchestratorRequest.getKycVerifiedDetails()).getDetails();
        ErrorDetails error = orchestratorRequest.getKycVerifiedDetails().getError();

        if(error.isNotNull()) {
            verifierResponse.setIsVerified(false);
            verifierResponse.setComments(error.getMessage());
            return verifierResponse;
        }

        KycDocDetails<?> kycDocDetails = kycDocDetailsManager.findKycDocDetailsByTenantIdAndDocType(RequestData.getTenantId(), DocType.GST);

        if(kycDocDetails != null) {
            GstDocDetails gstDocDetails = (GstDocDetails) kycDocDetails.getDetails();
            String gstin = gstDocDetails.getGstin();
            for (PanGstCrossVerifiedDetails.GstDetails gstDetails : panGstCrossVerifiedDetails.getGstDetails()) {
                if(gstin.equalsIgnoreCase(gstDetails.getGstNumber()) && "Active".equalsIgnoreCase(gstDetails.getGstinStatus())){
                    verifierResponse.setIsVerified(true);
                    return verifierResponse;
                }
            }
        }

        return verifierResponse;
    }
}
