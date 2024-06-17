package com.sr.capital.entity.primary;


import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.PROVIDER_CONFIG_TEMPLATE;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = PROVIDER_CONFIG_TEMPLATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class ProviderTemplateConfigEntity extends LongBaseEntity {

    @Column(name = "partner_id")
    Long partnerId;

    String source;

    String destination;

    String type;

    String group;
}
