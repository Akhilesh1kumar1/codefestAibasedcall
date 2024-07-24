package com.sr.capital.kyc.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.BankAccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankDetailsRequest {

    //@NotBlank(message = "Name cannot be null or empty!!!")
    private String name;


    //@NotBlank(message = "Ifsc code cannot be null or empty!!!")
    private String ifscCode;

    //@NotBlank(message = "Bank name cannot be null or empty!!!")
    private String bankName;

    Long baseBankId;

    BankAccountType bankAccountType;

    //@NotEmpty(message = "accountNumber cannot be null")
    String accountNumber;

    String accountHolderName;

    String accountStatementLink;

    String statementPassword;

    @Builder.Default
    Boolean isAccountVerified = false;

    String bankAccountPassword;

}
