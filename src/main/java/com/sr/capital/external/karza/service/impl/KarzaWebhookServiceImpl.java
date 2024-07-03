package com.sr.capital.external.karza.service.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.kyc.GstCompleteDocDetails;
import com.sr.capital.entity.mongo.kyc.GstDocDetailsHistory;
import com.sr.capital.entity.primary.Task;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.karza.request.GstConsolidationWebhookRequest;
import com.sr.capital.external.karza.service.KarzaWebhookService;
import com.sr.capital.service.entityimpl.GstCompleteDetailsManager;
import com.sr.capital.service.entityimpl.GstCompleteHistoryManager;
import com.sr.capital.service.entityimpl.TaskManager;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KarzaWebhookServiceImpl implements KarzaWebhookService {

    final AppProperties appProperties;

    final TaskManager taskManager;

    final VerificationEntityServiceImpl verificationEntityService;

    final GstCompleteDetailsManager gstCompleteDetailsManager;

    final GstCompleteHistoryManager gstCompleteHistoryManager;

    @Override
    public Boolean saveGstDetails(GstConsolidationWebhookRequest gstConsolidationWebhookRequest, String vendorToken, String vendorCode) throws CustomException {
        validateRequest(gstConsolidationWebhookRequest,vendorCode,vendorToken);

        GstCompleteDocDetails gstCompleteDocDetails = gstCompleteDetailsManager.getGstDetailsByTenantIdAndGstInId(gstConsolidationWebhookRequest.getSrCompanyId(),gstConsolidationWebhookRequest.getResult().getGstin());
        if(gstCompleteDocDetails==null) {
             gstCompleteDocDetails = GstCompleteDocDetails.builder().gstInNumber(gstConsolidationWebhookRequest.getResult().getGstin()).completeGstDetails(gstConsolidationWebhookRequest).srCompanyId(gstConsolidationWebhookRequest.getSrCompanyId()).build();
        }else{
            gstCompleteDocDetails.setCompleteGstDetails(gstConsolidationWebhookRequest);
        }
        gstCompleteDetailsManager.saveGstDetails(gstCompleteDocDetails);

        GstDocDetailsHistory gstDocDetailsHistory = GstDocDetailsHistory.builder().srCompanyId(gstCompleteDocDetails.getSrCompanyId()).completeGstDetails(gstConsolidationWebhookRequest).gstIn(gstCompleteDocDetails.getGstInNumber()).build();
        gstCompleteHistoryManager.saveGstHistory(gstDocDetailsHistory);
        return true;
    }

    private void validateRequest(GstConsolidationWebhookRequest gstConsolidationWebhookRequest, String vendorCode, String vendorToken) throws CustomException {

        validateRequestToken(vendorCode,vendorToken);

        if(gstConsolidationWebhookRequest.getRequestId()==null){
            throw new CustomException("requestId cannot be null", HttpStatus.BAD_REQUEST);
        }

        Task task = taskManager.findTaskByRequestId(gstConsolidationWebhookRequest.getRequestId());

        if(task==null){
            throw new CustomException("Invalid requestId "+gstConsolidationWebhookRequest.getRequestId(), HttpStatus.BAD_REQUEST);
        }
        gstConsolidationWebhookRequest.setSrCompanyId(task.getGroupId());

    }


    private Boolean validateRequestToken(String vendorCode,String vendorToken) throws InvalidVendorTokenException, InvalidVendorCodeException {
        if(!appProperties.getKlubVendorCode().equalsIgnoreCase(vendorCode)){
            throw new InvalidVendorCodeException();
        }

        if(!appProperties.getKlubVendorToken().equalsIgnoreCase(vendorToken)){
            throw new InvalidVendorTokenException();
        }
        return true;
    }
}
