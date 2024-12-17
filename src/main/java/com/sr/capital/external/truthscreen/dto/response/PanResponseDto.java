package com.sr.capital.external.truthscreen.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PanResponseDto {

    int status;
    Map<String, Object> msg;
}
