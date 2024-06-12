package com.sr.capital.entity;

import com.sr.capital.helpers.enums.DirectorDesignation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.sr.capital.helpers.constants.Constants.EntityNames.DIRECTOR_KYC;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = DIRECTOR_KYC)
public class DirectorKycEntity extends LongBaseEntity{

    @Column(name = "user_id")
    Long capitalUserId;

    @Column(name = "sr_company_id")
    Long srCompanyId;

    @Enumerated(EnumType.STRING)
    DirectorDesignation designation;

    String panLink;

    @Column(name = "identification_link")
    String identificationLink;

    @Builder.Default
    @Column(name = "mark_as_authorized_signatory")
    Boolean markAsAuthorizedSignatory=false;

}
