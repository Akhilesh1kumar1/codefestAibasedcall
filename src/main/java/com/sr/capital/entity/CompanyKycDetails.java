package com.sr.capital.entity;

import com.sr.capital.helpers.enums.CompanyType;
import jakarta.persistence.Column;
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



}
