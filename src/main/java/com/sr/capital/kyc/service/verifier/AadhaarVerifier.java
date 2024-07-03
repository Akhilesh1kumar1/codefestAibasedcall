package com.sr.capital.kyc.service.verifier;


import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.primary.Task;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.ErrorDetails;
import com.sr.capital.exception.custom.TaskProcessingException;
import com.sr.capital.helpers.enums.TaskStatus;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import com.sr.capital.service.entityimpl.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AadhaarVerifier implements DetailsVerifier {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private TaskManager taskManager;

    @Override
    @SuppressWarnings("unchecked")
    public VerifierResponse verify(DocOrchestratorRequest orchestratorRequest) throws TaskProcessingException {

        VerifierResponse verifierResponse = VerifierResponse.builder().build();

        if(1==2){//kycAppProperties.getAadhaarVerificationRelyOnPan()

            VerificationEntity verification = orchestratorRequest.getVerification();
            Task task = null;//taskManager.findTaskByVerificationIdAndTaskType(verification.getId(), TaskType.PAN_AADHAAR);
            if(task == null){
                throw new TaskProcessingException();
            }
            TaskStatus taskStatus = task.getStatus();
            switch (taskStatus) {
                case PENDING_FOR_MANUAL_APPROVAL:
                    verifierResponse.setIsVerified(false);
                    break;
                case SUCCESS:
                    verifierResponse.setIsVerified(true);
                    break;
                default:
                    throw new TaskProcessingException();
            }

        } else {
            KycVerifiedDetails<AadhaarVerifiedDetails> aadhaarVerifiedDetails = (KycVerifiedDetails<AadhaarVerifiedDetails>) orchestratorRequest.getKycVerifiedDetails();
            ErrorDetails error = orchestratorRequest.getKycVerifiedDetails().getError();

            if(error.isNotNull()) {
                verifierResponse.setIsVerified(false);
                verifierResponse.setComments(error.getMessage());
                return verifierResponse;
            }
            Boolean flag = "id_found".equalsIgnoreCase(aadhaarVerifiedDetails.getDetails().getStatus());
            verifierResponse.setIsVerified(flag);
        }
        return verifierResponse;
    }
}
