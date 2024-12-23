package com.sr.capital.external.truthscreen.service.interfaces;

import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;

import java.io.IOException;

public interface TruthScreenResponseConstructor {

    IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request) throws IOException;

}
