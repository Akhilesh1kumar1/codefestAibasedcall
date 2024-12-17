package com.sr.capital.external.truthscreen.util;

import com.sr.capital.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TruthScreenUtil {

    @Autowired
    private AppProperties appProperties;

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("username",appProperties.getAuthBridgeUsername());
        headers.add("Content-Type","application/json");
        return headers;
    }
}
