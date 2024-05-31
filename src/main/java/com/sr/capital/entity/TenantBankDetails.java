package com.sr.capital.entity;

import com.sr.capital.config.AttributeEncryptor;
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
@Entity
@Table(name = TENANT_BANK_DETAILS)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class TenantBankDetails extends UUIDBaseEntity{

    @Column(name = "tenant_capital_id")
    Long tenantCapitalId;

    @Column(name = "user_id")
    Long userId;


    @Column(name = "base_bank_id")
    Long baseBankId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "account_number")
    String accountNumber;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "account_holder_name")
    String accountHolderName;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "ifsc_code")
    String ifscCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_type")
    BankAccountType bankAccountType;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "account_statement_link")
    String accountStatementLink;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "statement_password")
    String statementPassword;

    @Builder.Default
    @Column(name = "is_account_verified")
    private Boolean isAccountVerified = false;
}
