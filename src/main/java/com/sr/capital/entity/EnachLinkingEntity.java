package com.sr.capital.entity;

import com.sr.capital.dto.request.EnachLinkingRequestDto;
import com.sr.capital.helpers.enums.AccountDebitType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

import static com.sr.capital.helpers.constants.Constants.EntityNames.ENACH_LINKING;
import static com.sr.capital.helpers.constants.Constants.EntityNames.TENANT_BANK_DETAILS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = ENACH_LINKING)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class EnachLinkingEntity extends UUIDBaseEntity{

    @Column(name = "capital_bank_id")
    UUID capitalBankId;

    @Column(name = "debit_amount")
    BigDecimal debitAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_debit_type")
    AccountDebitType accountDebitType;


    public static EnachLinkingEntity mapEntity(EnachLinkingRequestDto enachLinkingRequestDto){
        EnachLinkingEntity enachLinkingEntity =  EnachLinkingEntity.builder().capitalBankId(enachLinkingRequestDto.getCapitalBankId()).accountDebitType(enachLinkingRequestDto.getAccountDebitType()).debitAmount(enachLinkingRequestDto.getDebitAmount()).build();

        enachLinkingEntity.setIsEnabled(true);
        return  enachLinkingEntity;
    }
}
