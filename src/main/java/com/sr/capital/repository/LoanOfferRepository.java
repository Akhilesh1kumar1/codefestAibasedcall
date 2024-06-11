package com.sr.capital.repository;

import com.sr.capital.entity.LoanOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanOfferRepository extends JpaRepository<LoanOffer, UUID> {

    List<LoanOffer> findBySrCompanyIdAndIsEnabled(Long srCompanyId,Boolean isEnabled);

    List<LoanOffer> findBySrCompanyIdAndStatusAndIsEnabled(Long srCompanyId,String status,Boolean isEnabled);


}
