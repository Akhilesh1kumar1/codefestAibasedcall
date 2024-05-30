package com.sr.capital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.ACCOUNT_TYPE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = ACCOUNT_TYPE)
public class AccountType extends LongBaseEntity{


    @Column(name = "account_type_name")
    String accountTypeName;

    @Column(name = "display_name")
    String displayName;

    @Column(name = "description")
    String description;
}
