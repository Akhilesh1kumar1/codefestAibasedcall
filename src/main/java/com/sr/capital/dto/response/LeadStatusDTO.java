package com.sr.capital.dto.response;

import lombok.Data;

@Data
public class LeadStatusDTO {
    private String key;
    private String value;


    public LeadStatusDTO(String name, String displayName) {
        this.key =name;
        this.value=displayName;
    }
}
