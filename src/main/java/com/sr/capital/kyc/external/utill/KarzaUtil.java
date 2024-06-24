package com.sr.capital.kyc.external.utill;


import com.sr.capital.config.AppProperties;
import com.sr.capital.kyc.external.constants.KarzaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KarzaUtil {

    @Autowired
    private AppProperties kycAppProperties;

    public HttpHeaders getKarzaHeader() {
        HttpHeaders header = new HttpHeaders();

        header.add(KarzaConstant.KARZA_ACCOUNT_ID, "");//kycAppProperties.getIdfyAccountId()
        header.add(KarzaConstant.KARZA_API_KEY,"" );//kycAppProperties.getIdfyApiKey()
        header.add(KarzaConstant.CONTENT_TYPE, KarzaConstant.APPLICATION_JSON);

        return header;
    }

    public Map<String, String> getTaskParameter(String value) {
        return Map.of(KarzaConstant.REQUEST_ID, value);
    }

}
