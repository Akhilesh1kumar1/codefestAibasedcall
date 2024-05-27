package com.sr.capital.repository;

import com.sr.capital.entity.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends CrudRepository<Bank,Long> {

    Bank findByBankName(String bankName);
}
