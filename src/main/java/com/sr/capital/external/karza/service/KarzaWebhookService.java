package com.sr.capital.external.karza.service;

import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.karza.request.GstConsolidationWebhookRequest;

public interface KarzaWebhookService {

    Boolean saveGstDetails(GstConsolidationWebhookRequest gstConsolidationWebhookRequest,String vendorToken,String vendorCode) throws CustomException;
}
