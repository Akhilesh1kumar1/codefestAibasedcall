package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.Pincode;
import com.sr.capital.repository.primary.PincodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PincodeEntityServiceImpl {

    final PincodeRepository pincodeRepository;


    public Pincode getPincodeDetailsByVendorId(Long pincode,Long loanVendorId){
        return pincodeRepository.findByPincodeAndLoanVendorId(pincode,loanVendorId);
    }
}
