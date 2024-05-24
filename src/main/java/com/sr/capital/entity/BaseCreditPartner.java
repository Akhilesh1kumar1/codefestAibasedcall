package com.sr.capital.entity;

import com.sr.capital.dto.request.CreateBaseCreditPartnerDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.BASE_CREDIT_PARTNER_TABLE_NAME;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = BASE_CREDIT_PARTNER_TABLE_NAME)
public class BaseCreditPartner extends LongBaseEntity{

    @Column(name = "credit_partner_name")
    String creditPartnerName;

    @Column(name = "description")
    String description;

    @Column(name = "image_link")
    String imageLink;

    @Column(name = "display_name")
    String displayName;


    public static BaseCreditPartner mapBaseCreditPartnerFromDto(CreateBaseCreditPartnerDto createBaseCreditPartnerDto){
        BaseCreditPartner baseCreditPartner =  BaseCreditPartner.builder().creditPartnerName(createBaseCreditPartnerDto.getCreditPartnerName()).displayName(createBaseCreditPartnerDto.getDisplayName()).description(createBaseCreditPartnerDto.getImageLink()).build();
        baseCreditPartner.setIsEnabled(createBaseCreditPartnerDto.getEnabled());
        return baseCreditPartner;
    }

}
