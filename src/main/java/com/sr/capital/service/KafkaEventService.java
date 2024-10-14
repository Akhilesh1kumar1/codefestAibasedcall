package com.sr.capital.service;

public interface KafkaEventService {

    <T,U> T handleEvents(U request) throws Exception ;
}
