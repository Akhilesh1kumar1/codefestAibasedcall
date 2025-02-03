package com.sr.capital.external.corpveda.service;

import com.sr.capital.external.corpveda.dto.request.CorpVedaRequestDto;
import com.sr.capital.external.corpveda.dto.response.CorpVedaResponseDto;

public interface CorpVedaService {

    public CorpVedaResponseDto getCinDetails(CorpVedaRequestDto requestDto) throws Exception;

}
