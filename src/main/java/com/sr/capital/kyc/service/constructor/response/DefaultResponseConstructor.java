package com.sr.capital.kyc.service.constructor.response;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.response.DocUploadResponseDto;
import com.sr.capital.external.common.request.DocumentUploadRequestDto;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

@Service
public class DefaultResponseConstructor implements ResponseConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {
       /* String message;
        if(input != null){
            message = (String) input;
        } else {
            message = "OK!";
        }*/
        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        DocUploadResponseDto<?> docUploadResponseDto =DocUploadResponseDto.builder().kycDocDetails(orchestratorRequest.getKycDocDetails())
                .imageUrl(orchestratorRequest.getFile1()!=null?orchestratorRequest.getFile1().getPreSignedUri():null)
                .build();
        GenericResponse<T> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData((T) docUploadResponseDto);

        return new ResponseEntity<>(
            response,
            HttpStatus.OK
        );
    }

}
