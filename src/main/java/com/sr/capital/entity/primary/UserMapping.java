package com.sr.capital.entity.primary;

import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.USER_MAPPING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = USER_MAPPING)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class UserMapping extends LongBaseEntity{

    @Column(name = "capital_user_id")
    private Long capitalUserId;

    @Column(name = "sr_user_id")
    private Long srUserId;

    @Column(name = "sr_company_id")
    private Long srCompanyId;

}
