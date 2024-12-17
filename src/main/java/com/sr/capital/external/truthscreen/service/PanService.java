package com.sr.capital.external.truthscreen.service;

import com.sr.capital.external.truthscreen.dto.request.AsyncRequestDto;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.AsyncReponseDto;
import com.sr.capital.external.truthscreen.dto.response.PanResponseDto;

public interface PanService {

    PanResponseDto sendPanRequest(IdSearchRequestDto requestDTO);

    AsyncReponseDto getAsyncStatus(AsyncRequestDto asyncRequestDto);
}
