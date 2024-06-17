package com.sr.capital.entity.primary;

import com.sr.capital.dto.request.CreateBaseBankDto;
import com.sr.capital.dto.request.CreateBaseCreditPartnerDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.BANK;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = BANK)
public class Bank extends LongBaseEntity{

    @Column(name = "bank_name")
    String bankName;

    @Column(name = "description")
    String description;

    @Column(name = "image_link")
    String imageLink;

    @Column(name = "display_name")
    String displayName;


    public static Bank mapBankDetailsFromDto(CreateBaseBankDto createBaseBankDto){
        Bank bank =  Bank.builder().bankName(createBaseBankDto.getBankName()).displayName(createBaseBankDto.getDisplayName()).description(createBaseBankDto.getImageLink()).build();
        bank.setIsEnabled(createBaseBankDto.getEnabled());
        return bank;
    }
}
