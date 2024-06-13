package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class BankVerifiedDetails implements Serializable {

    @Field("name_at_bank")
    private String nameAtBank;

    @Field("bank_account_number")
    private String bankAccountNumber;

    @Field("ifsc_code")
    private String ifscCode;

    @Field("account_exists")
    private String accountExists;

    @Field("amount_deposited")
    private String amountDeposited;

    @Field("status")
    private String status;
}
