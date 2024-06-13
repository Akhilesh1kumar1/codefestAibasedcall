package com.sr.capital.kyc.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankDetailsRequest {

    @NotBlank(message = "Name cannot be null or empty!!!")
    private String name;

    @NotBlank(message = "Account number cannot be null or empty!!!")
    private String accountNo;

    @NotBlank(message = "Ifsc code cannot be null or empty!!!")
    private String ifscCode;

    @NotBlank(message = "Bank name cannot be null or empty!!!")
    private String bankName;

}
