package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanStatusUpdateHandlerServiceImpl {


    public void handleStatusUpdate(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto,String loanVendorName){


    }
}
