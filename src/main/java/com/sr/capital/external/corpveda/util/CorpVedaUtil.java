package com.sr.capital.external.corpveda.util;


import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class CorpVedaUtil {

    @Autowired
    private RedissonClient redissonClient;

    public HttpHeaders setHeaders(String token){
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Authorization","Bearer "+token);
        return headers;
    }

}
