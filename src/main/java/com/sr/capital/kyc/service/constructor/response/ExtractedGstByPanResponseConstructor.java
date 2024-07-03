package com.sr.capital.kyc.service.constructor.response;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstByPanDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.FetchDocDetailsResponse;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

@Service
public class ExtractedGstByPanResponseConstructor implements ResponseConstructor {
    @Override
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {
        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        KycDocDetails<GstByPanDocDetails> kycDocDetails = (KycDocDetails<GstByPanDocDetails>) orchestratorRequest.getKycDocDetails();
        FetchDocDetailsResponse<GstByPanDocDetails> responseData = FetchDocDetailsResponse.<GstByPanDocDetails>builder().srCompanyId(RequestData.getTenantId())
                .docType(orchestratorRequest.getDocType()).details(kycDocDetails.getDetails()).build();
        GenericResponse<T> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData((T) responseData);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}
