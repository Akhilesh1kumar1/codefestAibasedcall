package com.sr.capital.entity;

import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.ADHAR_DETAILS;
import static com.sr.capital.helpers.constants.Constants.EntityNames.USER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = ADHAR_DETAILS)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class AdharDetails extends LongBaseEntity{

    @Column(name = "user_id")
    Long userId;

    @Column(name = "adhar_number")
    String adharNumber;

    @Column(name = "name_on_adhar")
    String nameOnAdhar;

    String dateOfBirth;

    
}
