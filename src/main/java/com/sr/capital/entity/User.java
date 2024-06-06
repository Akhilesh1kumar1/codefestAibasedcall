package com.sr.capital.entity;

import com.sr.capital.config.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.USER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = USER)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class User extends LongBaseEntity{




    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "first_name")
    private String firstName;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "last_name")
    private String lastName;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "email")
    private String email;

    @Builder.Default
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Builder.Default
    @Column(name = "country_code")
    private String countryCode = "+91";

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "mobile")
    private String mobile;

    @Builder.Default
    @Column(name = "is_mobile_verified")
    private Boolean isMobileVerified = false;


    @Column(name = "comments")
    private String comments;

}
