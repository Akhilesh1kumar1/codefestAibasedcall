package com.sr.capital.entity.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.config.AttributeEncryptor;
import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.dto.request.CreateBaseBankDto;
import com.sr.capital.dto.response.TenantBankResponseDto;
import com.sr.capital.helpers.enums.BankAccountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.sr.capital.helpers.constants.Constants.EntityNames.TENANT_BANK_DETAILS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = TENANT_BANK_DETAILS)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class TenantBankDetails extends UUIDBaseEntity{

    @Column(name = "sr_company_id")
    Long srCompanyId;

    @Column(name = "base_bank_id")
    Long baseBankId;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "account_number")
    String accountNumber;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "account_holder_name")
    String accountHolderName;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "ifsc_code")
    String ifscCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_type")
    BankAccountType bankAccountType;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "account_statement_link")
    String accountStatementLink;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "statement_password")
    String statementPassword;

    @Builder.Default
    @Column(name = "is_account_verified")
    private Boolean isAccountVerified = false;


    public static TenantBankDetails mapBankDetailsFromDto(BankDetailsRequestDto bankDetailsRequestDto){
        TenantBankDetails bank =  TenantBankDetails.builder().srCompanyId(bankDetailsRequestDto.getSrCompanyId()).baseBankId(bankDetailsRequestDto.getBaseBankId())
                .accountHolderName(bankDetailsRequestDto.getAccountHolderName())
                .accountNumber(bankDetailsRequestDto.getAccountNumber())
                .ifscCode(bankDetailsRequestDto.getIfscCode())
                .isAccountVerified(bankDetailsRequestDto.getIsAccountVerified()).statementPassword(bankDetailsRequestDto.getStatementPassword())
                .accountStatementLink(bankDetailsRequestDto.getAccountStatementLink())
                .bankAccountType(bankDetailsRequestDto.getBankAccountType())
                .build();
        bank.setIsEnabled(true);
        return bank;
    }


}
