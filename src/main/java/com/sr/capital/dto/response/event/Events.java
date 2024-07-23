package com.sr.capital.dto.response.event;

import lombok.Data;

import java.util.Map;

@Data
public class Events {
    Map<String, Event> events;
}
