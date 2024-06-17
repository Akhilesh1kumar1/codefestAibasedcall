package com.sr.capital.validation;

import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoanApplicationRequestValidator  implements RequestValidator {

    final LoanApplicationRepository loanApplicationRepository;

    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        return null;
    }
}
