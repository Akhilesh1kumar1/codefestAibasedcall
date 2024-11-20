package com.sr.capital.external.common;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;

public interface StatusMapperInterface {

    LoanStatusUpdateWebhookDto mapStatus(Object loanApplicationDetails);
}
