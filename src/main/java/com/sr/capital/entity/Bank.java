package com.sr.capital.entity;

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
}
