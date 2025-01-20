package com.sr.capital.external.corpveda.util;

import com.sr.capital.config.AppProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

public class CorpVedaUtil {

    public HttpHeaders getHeaders(String token){
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("token",token);
        return headers;
    }
}
