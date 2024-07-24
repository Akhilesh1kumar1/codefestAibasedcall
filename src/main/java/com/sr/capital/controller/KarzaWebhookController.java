package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.karza.request.GstConsolidationWebhookRequest;
import com.sr.capital.external.karza.service.KarzaWebhookService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/karza")
@Validated
@RequiredArgsConstructor
public class KarzaWebhookController {

    final KarzaWebhookService karzaWebhookService;


    @PostMapping("/gst/advance")
    public GenericResponse<Boolean> saveLoanOffer(@RequestBody GstConsolidationWebhookRequest gstConsolidationWebhookRequest, @RequestHeader(name = "x-vendor-token") String vendorToken, @RequestHeader(name = "x-vendor-code") String vendorCode) throws CustomException {
        return ResponseBuilderUtil.getResponse(karzaWebhookService.saveGstDetails(gstConsolidationWebhookRequest,vendorToken,vendorCode), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

}
