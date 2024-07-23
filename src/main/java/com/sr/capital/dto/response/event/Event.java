package com.sr.capital.dto.response.event;

import lombok.Data;

import java.util.Map;

@Data
public class Event {
    Map<Transitions, Action> transition;

}
