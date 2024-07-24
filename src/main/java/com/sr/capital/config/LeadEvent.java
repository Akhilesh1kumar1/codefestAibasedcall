package com.sr.capital.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sr.capital.dto.response.event.Events;
import com.sr.capital.dto.response.event.Transitions;
import com.sr.capital.util.TransitionsKeyDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
public class LeadEvent {

    @Bean(name = "leadEvents")
    public Events getEventsResource() throws JsonProcessingException {
        String str="";
        try {
            try (InputStream inputStream = getClass().getResourceAsStream("/LeadStatus.json");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                str= reader.lines()
                        .collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper= new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(Transitions.class, new TransitionsKeyDeserializer());
        objectMapper.registerModule(simpleModule);
        Events events=objectMapper.readValue(str,Events.class);
        return events;

    }
}
