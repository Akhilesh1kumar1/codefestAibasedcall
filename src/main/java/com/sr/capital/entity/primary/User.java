package com.sr.capital.entity.primary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.config.AttributeEncryptor;
import com.sr.capital.dto.request.UserDetails;
import jakarta.persistence.*;
import lombok.*;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

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


    @Column(name = "sr_user_id")
    private Long srUserId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "last_name")
    private String lastName;

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "email")
    private String email;

    @Builder.Default
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Builder.Default
    @Column(name = "country_code")
    private String countryCode = "+91";

    //@convert(converter = AttributeEncryptor.class)
    @Column(name = "mobile")
    private String mobile;



    @Column(name = "comments")
    private String comments;

    @Column(name = "sr_company_id")
    private Long srCompanyId;

    @Builder.Default
    @Column(name = "is_accepted")
    private Boolean isAccepted = false;

    public static User mapUser(UserDetails userDetails){
        User user =User.builder().srUserId(Long.valueOf(userDetails.getUserId())).
        comments(userDetails.getComments())
                .isAccepted(userDetails.getIsAccepted()) .firstName(userDetails.getFirstName()).middleName(userDetails.getMiddleName()).lastName(userDetails.getLastName()).email(userDetails.getEmail()).mobile(userDetails.getMobileNumber()).build();
        user.setIsEnabled(true);
        return user;
    }

}
