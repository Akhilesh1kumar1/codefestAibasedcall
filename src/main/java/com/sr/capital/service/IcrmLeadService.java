package com.sr.capital.service;

import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.response.IcrmLeadRsponseDto;
import com.sr.capital.exception.custom.CustomException;

import java.io.IOException;
import java.text.ParseException;

public interface IcrmLeadService {

    public IcrmLeadRsponseDto getLeadDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException;
}
