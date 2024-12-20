package com.sr.capital.external.truthscreen.service;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.AsyncRequestDto;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.AsyncReponseDto;
import org.apache.poi.ss.formula.functions.T;

public interface PanService {

    T sendPanRequest(IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException;

    AsyncReponseDto getAsyncStatus(AsyncRequestDto asyncRequestDto);
}
