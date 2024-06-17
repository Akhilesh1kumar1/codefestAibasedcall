package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.AccountType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends CrudRepository<AccountType,Long> {

    AccountType findByAccountTypeName(String accountTypeName);
}
