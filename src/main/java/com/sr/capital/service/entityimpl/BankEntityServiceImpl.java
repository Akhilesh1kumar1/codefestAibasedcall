package com.sr.capital.service.entityimpl;

import com.sr.capital.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BankEntityServiceImpl {

    final BankRepository bankRepository;
}
