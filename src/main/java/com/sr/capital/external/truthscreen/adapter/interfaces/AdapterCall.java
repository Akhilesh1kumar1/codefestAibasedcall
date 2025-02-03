package com.sr.capital.external.truthscreen.adapter.interfaces;

public interface AdapterCall {

    public <T, U> T extractDetails(U request) throws Exception;
}
