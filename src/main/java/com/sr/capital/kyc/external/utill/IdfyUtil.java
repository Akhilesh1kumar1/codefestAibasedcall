package com.sr.capital.kyc.external.utill;


import com.sr.capital.config.AppProperties;
import com.sr.capital.kyc.external.constants.IdfyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdfyUtil {

    @Autowired
    private AppProperties kycAppProperties;

    public HttpHeaders getIDfyHeader() {
        HttpHeaders header = new HttpHeaders();

        header.add(IdfyConstant.IDFY_ACCOUNT_ID, "");//kycAppProperties.getIdfyAccountId()
        header.add(IdfyConstant.IDFY_API_KEY,"" );//kycAppProperties.getIdfyApiKey()
        header.add(IdfyConstant.CONTENT_TYPE, IdfyConstant.APPLICATION_JSON);

        return header;
    }

    public Map<String, String> getTaskParameter(String value) {
        return Map.of(IdfyConstant.REQUEST_ID, value);
    }

}
