package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.Pincode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PincodeRepository extends CrudRepository<Pincode,Long> {

    Pincode findByPincodeAndLoanVendorId(Long pincode , Long loanVendorId);
}
