package com.sr.capital.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CibilScoreCheckRequestDto extends UserDetails{

    @NotNull(message = "PAN cannot be null")
    String panNumber;

}
