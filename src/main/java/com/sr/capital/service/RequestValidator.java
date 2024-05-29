package com.sr.capital.service;

public interface RequestValidator {
    <T,U> T validateRequest(U request) throws Exception ;
}