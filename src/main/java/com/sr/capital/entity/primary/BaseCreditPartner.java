package com.sr.capital.entity.primary;

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

    @Column(name = "provide_pre_approved_loan")
    Boolean providePreApprovedLoan;

    @Column(name = "provide_term_loan")
    Boolean provideTermLoan;

    @Column(name = "provide_rbf")
    Boolean provideRBF ;//Revenue Based Financing

    @Column(name = "line_of_credit")
    Boolean lineOfCredit;

    @Column(name = "loan_against_property")
    Boolean loanAgainstProperty;

    @Column(name = "invoice_financing")
    Boolean invoiceFinancing;

    @Column(name = "business_credit_card")
    Boolean businessCreditCard;

    @Column(name = "gst")
    String gst;
    public static BaseCreditPartner mapBaseCreditPartnerFromDto(CreateBaseCreditPartnerDto createBaseCreditPartnerDto){
        BaseCreditPartner baseCreditPartner =  BaseCreditPartner.builder().creditPartnerName(createBaseCreditPartnerDto.getCreditPartnerName()).displayName(createBaseCreditPartnerDto.getDisplayName()).description(createBaseCreditPartnerDto.getImageLink()).imageLink(createBaseCreditPartnerDto.getImageLink()).build();
        baseCreditPartner.setIsEnabled(createBaseCreditPartnerDto.getEnabled());
        return baseCreditPartner;
    }

}
