package com.sr.capital.external.truthscreen.validator.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import com.sr.capital.external.truthscreen.validator.TruthScreenRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GstinValidator implements TruthScreenRequestValidator {

    @Autowired
    private TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;

    @Override
    public <T, U> T validateRequest(U request, TruthScreenDocDetails truthScreenDocDetails) throws Exception {
        if (truthScreenDocDetails == null){
            TruthScreenDocDetails truthScreenDocDetailsPreviousDocDetails = truthScreenDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(RequestData.getTenantId(), TruthScreenDocType.PAN_TO_GST);
            if (truthScreenDocDetailsPreviousDocDetails != null){
                if (request instanceof TruthScreenDocOrchestratorRequest truthScreenDocOrchestratorRequest){
                    truthScreenDocOrchestratorRequest.setPreviousDocDetails(truthScreenDocDetailsPreviousDocDetails);
                    return (T) truthScreenDocOrchestratorRequest;
                }
                else {
                    throw new Exception("TruthScreenOrchestrationRequest not found");
                }
            }
            else {
                    throw new Exception("Previous Doc Details not found");
            }
        }
        else {
            return null;
        }
    }
}
