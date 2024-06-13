package com.sr.capital.kyc.service.strategy;


import com.sr.capital.entity.Task;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.exception.custom.TaskProcessingException;
import com.sr.capital.exception.custom.VerifierNotFound;
import com.sr.capital.helpers.enums.DocStatus;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.external.exception.IDfyNameComparisonException;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import com.sr.capital.kyc.service.verifier.*;
import com.sr.capital.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifierStrategy {

    @Autowired
    private AadhaarVerifier aadhaarVerifier;

    @Autowired
    private BankDetailsVerifier bankDetailsVerifier;

    @Autowired
    private GstVerifier gstVerifier;

    @Autowired
    private PanAadhaarVerifier panAadhaarVerifier;

    @Autowired
    private PanBankVerifier panBankVerifier;

    @Autowired
    private PanGstVerifier panGstVerifier;

    @Autowired
    private PanVerifier panVerifier;

    public LoggerUtil loggerUtil  =LoggerUtil.getLogger(VerifierStrategy.class);

    public VerifierResponse verifyDetails(DocOrchestratorRequest docOrchestratorRequest)
            throws VerifierNotFound, IDfyNameComparisonException, ServiceEndpointNotFoundException, TaskProcessingException {
        Task task = docOrchestratorRequest.getTask();
        DetailsVerifier detailsVerifier;
        switch (task.getType()) {
            case AADHAAR:
                detailsVerifier = aadhaarVerifier;
                break;
            case BANK_DETAILS:
                detailsVerifier = bankDetailsVerifier;
                break;
            case GST:
                detailsVerifier = gstVerifier;
                break;
            case PAN_AADHAAR:
                detailsVerifier = panAadhaarVerifier;
                break;
            case PAN_BANK_DETAILS:
                detailsVerifier = panBankVerifier;
                break;
            case PAN_GST:
                detailsVerifier = panGstVerifier;
                break;
            case PAN:
                detailsVerifier = panVerifier;
                break;
            default:
                throw new VerifierNotFound();
        }
        VerifierResponse verifierResponse = detailsVerifier.verify(docOrchestratorRequest);
        loggerUtil.info("VERIFICATION RESPONSE for Task: " + task + " Status: " + verifierResponse.toString());

        if(verifierResponse.getIsVerified()){
            loggerUtil.info("Got verified, marking doc as verified");
            switch (task.getType()){
                case AADHAAR:
                case PAN:
                case BANK_DETAILS:
                case GST:
                    loggerUtil.info("Doc marking" + task.getType() + " as verified");
                    docOrchestratorRequest.getKycDocDetails().setStatus(DocStatus.VERIFIED);
            }
        }
        return verifierResponse;
    }
}
