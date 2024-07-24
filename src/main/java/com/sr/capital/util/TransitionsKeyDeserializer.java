package com.sr.capital.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.dto.response.event.Transitions;

import java.io.IOException;

public class TransitionsKeyDeserializer extends KeyDeserializer {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Transitions deserializeKey(final String key, final DeserializationContext ctxt ) throws IOException, JsonProcessingException
    { ;
        return objectMapper.readValue(key, Transitions.class);
    }
}
