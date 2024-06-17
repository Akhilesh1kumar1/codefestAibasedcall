package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends CrudRepository<Bank,Long> {

    Bank findByBankName(String bankName);
}
