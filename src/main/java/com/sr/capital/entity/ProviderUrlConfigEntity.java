package com.sr.capital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.PROVIDER_URL_CONFIG;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = PROVIDER_URL_CONFIG)
@Inheritance(strategy = InheritanceType.JOINED)
public class ProviderUrlConfigEntity  extends LongBaseEntity{

    @Column(name = "partner_id")
    Long partnerId;

    @NotNull
    @Column(name = "`key`")
    private String key;

    @NotNull
    @Column(name = "`value`")
    private String value;

    @NotNull
    @Column(name ="`type`")
    private String partnerConfigTypes;


    @NotNull
    @Column(name ="`group`")
    private String partnerConfigGroups;


    @NotNull
    @Column(name ="priority")
    private Integer priority;
}
