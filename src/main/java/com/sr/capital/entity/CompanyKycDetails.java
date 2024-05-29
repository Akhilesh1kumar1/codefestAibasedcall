package com.sr.capital.entity;

import com.sr.capital.config.AttributeEncryptor;
import com.sr.capital.helpers.enums.CompanyType;
import com.sr.capital.helpers.enums.ProofOfIdentity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.sr.capital.helpers.constants.Constants.EntityNames.COMPANY_DETAILS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = COMPANY_DETAILS)
public class CompanyKycDetails extends UUIDBaseEntity{

    @Column(name = "tenant_capital_id")
    Long tenantCapitalId;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "company_type")
    CompanyType companyType;

    @Column(name = "proof_of_identity")
    ProofOfIdentity proofOfIdentity;

    @Column(name = "adhar_id")
    Long adharId;

    @Column(name = "pan_id")
    Long panId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "proof_image_link")
    String proofImageLink;
}
