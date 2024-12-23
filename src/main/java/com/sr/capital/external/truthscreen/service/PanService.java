package com.sr.capital.external.truthscreen.service;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;

public interface PanService {

    IdSearchResponseDto<?> sendPanRequest(IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException;

}
