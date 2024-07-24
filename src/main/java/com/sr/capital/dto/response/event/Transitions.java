package com.sr.capital.dto.response.event;

import com.sr.capital.helpers.enums.LeadStatus;
import lombok.Data;

import java.util.Objects;

@Data
public class Transitions {
    LeadStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transitions that = (Transitions) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash( status);
    }

    public Transitions( LeadStatus status) {
        this.status = status;
    }

    public Transitions(){}
}
