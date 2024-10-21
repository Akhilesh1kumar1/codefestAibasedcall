package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.response.SanctionDto;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SanctionServiceImpl {

    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;


    public SanctionDto fetchSanctionDetails(LoanMetaDataDto loanMetaDataDto){

        return null;
    }

}
