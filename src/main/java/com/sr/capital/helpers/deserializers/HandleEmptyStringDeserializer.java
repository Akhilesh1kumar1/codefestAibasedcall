package com.sr.capital.helpers.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class HandleEmptyStringDeserializer<T> extends StdDeserializer<T> {

    protected HandleEmptyStringDeserializer() {
        this(null);
    }

    protected HandleEmptyStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        String val = node.asText();

        if (val!= "") {
            return super.deserialize(p, ctxt, null);
        }
        return null;
    }

    
}
