package com.sr.capital.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateLeadResponseDto {

    String loanId;

    String clientLoanId;

    String status;

    String leadCode;

    Boolean success;


}
