package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.helpers.enums.LoanVendorName;
import org.springframework.stereotype.Service;

@Service
public class LoanAllocationServiceImpl {


    public void getLoanVendor(LoanMetaDataDto loanMetaDataDto){
        loanMetaDataDto.setLoanVendorName(LoanVendorName.FLEXI.getLoanVendorName().toLowerCase());
    }
}
