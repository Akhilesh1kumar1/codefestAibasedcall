package com.sr.capital.service.entityimpl;

import com.sr.capital.repository.AccountTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class AccountTypeEntityServiceImpl {

    final AccountTypeRepository accountTypeRepository;


}
