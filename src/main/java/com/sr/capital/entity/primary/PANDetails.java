package com.sr.capital.entity.primary;

import com.sr.capital.config.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.PAN_DETAILS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = PAN_DETAILS)
@Inheritance(strategy = InheritanceType.JOINED)
public class PANDetails extends LongBaseEntity{

    @Column(name = "user_id")
    Long userId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "pan")
    String panNumber;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "pan_image_link_1")
    String panImageLink1;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "pan_image_link_2")
    String panImageLink2;

    @Builder.Default
    Boolean isPanVerified =false;

}
