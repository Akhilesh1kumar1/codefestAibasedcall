package com.sr.capital.repository;

import com.sr.capital.entity.BaseCreditPartner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCreditPartnerRepository extends CrudRepository<BaseCreditPartner,Long> {

    BaseCreditPartner findByCreditPartnerName(String creaditPartnerName);
}
