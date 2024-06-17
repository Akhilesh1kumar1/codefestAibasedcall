package com.sr.capital.entity.primary;

import com.sr.capital.config.AttributeEncryptor;
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

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "adhar_number")
    String adharNumber;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "name_on_adhar")
    String nameOnAdhar;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "date_of_birth")
    String dateOfBirth;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "adhar_image_link_1")
    String adharImageLink1;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "adhar_image_link_2")
    String adharImageLink2;

    @Builder.Default
    Boolean isAdharVerified =false;

}
