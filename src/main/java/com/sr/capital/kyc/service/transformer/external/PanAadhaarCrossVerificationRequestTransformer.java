package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.request.verification.PanAadhaarCrossVerificationRequest;
import com.sr.capital.kyc.external.request.verification.data.PanAadhaarCrossVerificationData;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PanAadhaarCrossVerificationRequestTransformer implements ExternalRequestTransformer {

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdfyBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        KycDocDetails<PanDocDetails> kycDocDetails = (KycDocDetails<PanDocDetails>) request.getKycDocDetails();
        PanDocDetails panDocDetails = kycDocDetails.getDetails();
        KycDocDetails<AadhaarDocDetails> aadhaarDocDetailsKycDocDetails =
                (KycDocDetails<AadhaarDocDetails>) kycDocDetailsManager.findKycDocDetailsByTenantIdAndDocType(RequestData.getTenantId(), DocType.AADHAAR);
        AadhaarDocDetails aadhaarDocDetails = aadhaarDocDetailsKycDocDetails.getDetails();

        //TODO: Find a way to add aadhar here
        PanAadhaarCrossVerificationData panAadhaarCrossVerificationData = PanAadhaarCrossVerificationData.builder()
                .panNumber(panDocDetails.getIdNumber())
                .aadhaarNumber(aadhaarDocDetails.getIdNumber())
                .build();

        return (T) PanAadhaarCrossVerificationRequest.builder()
                .taskId(request.getTask().getId().toString())
                .groupId(request.getTask().getGroupId().toString())
                .data(panAadhaarCrossVerificationData)
                .build();
    }
}
