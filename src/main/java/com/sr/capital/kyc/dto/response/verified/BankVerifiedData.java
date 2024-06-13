package com.sr.capital.kyc.dto.response.verified;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankVerifiedData {

    private String nameAtBank;

    private String bankAccountNumber;

    private String ifscCode;

    private String accountExists;

    private String amountDeposited;

    private String status;

    private boolean isActive;

    private String expiresAt;

}
