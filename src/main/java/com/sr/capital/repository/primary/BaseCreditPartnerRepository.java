package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.BaseCreditPartner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCreditPartnerRepository extends CrudRepository<BaseCreditPartner,Long> {

    BaseCreditPartner findByCreditPartnerName(String creaditPartnerName);
}
