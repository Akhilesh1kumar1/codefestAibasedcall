package com.sr.capital.repository;

import com.sr.capital.entity.AccountType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends CrudRepository<AccountType,Long> {
}
