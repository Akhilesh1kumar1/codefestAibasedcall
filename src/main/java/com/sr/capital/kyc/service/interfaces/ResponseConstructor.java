package com.sr.capital.kyc.service.interfaces;

import com.omunify.core.model.GenericResponse;
import org.springframework.http.ResponseEntity;

public interface ResponseConstructor {

    <T,U> ResponseEntity<GenericResponse<T>> constructResponse(U input);
}
