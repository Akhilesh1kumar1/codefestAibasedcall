package com.sr.capital.entity.primary;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.UserDetails;
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

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "comments")
    private String comments;

    @Column(name = "sr_company_id")
    private Long srCompanyId;

    @Builder.Default
    @Column(name = "is_accepted")
    private Boolean isAccepted = false;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "pan_number")
    private String panNumber;

    @Builder.Default
    @Column(name = "is_mobile_verified")
    private Boolean isMobileVerified = false;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "current_account_available")
    private Boolean currentAccountAvailable;

    public static User mapUser(UserDetails userDetails){
        User user =User.builder().srUserId(Long.valueOf(userDetails.getUserId())).
        comments(userDetails.getComments()).srCompanyId(Long.valueOf(RequestData.getTenantId()))
                .isAccepted(userDetails.getIsAccepted()) .firstName(userDetails.getFirstName()).middleName(userDetails.getMiddleName()).lastName(userDetails.getLastName()).email(userDetails.getEmail()).mobile(userDetails.getMobileNumber()).entityType(userDetails.getEntityType()).panNumber(userDetails.getPanNumber()).dateOfBirth(userDetails.getDateOfBirth()).fatherName(userDetails.getFatherName())
                .gender(userDetails.getGender()).currentAccountAvailable(userDetails.getCurrentAccountAvailable()).build();
        user.setIsEnabled(true);
        if(userDetails.getCompanyName()!=null){
            user.setCompanyName(userDetails.getCompanyName());
        }
        user.setIsMobileVerified(userDetails.getIsMobileNumberVerified());
        return user;
    }

    public static void mapUpdateUser(UserDetails userDetails, User user) {
        user.setComments(userDetails.getComments());
        user.setIsAccepted(userDetails.getIsAccepted());
        user.setFirstName(userDetails.getFirstName());
        user.setMiddleName(userDetails.getMiddleName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setMobile(userDetails.getMobileNumber());
        user.setEntityType(userDetails.getEntityType());
        //user.setCompanyName(userDetails.getCompanyName());
        user.setPanNumber(userDetails.getPanNumber());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        user.setFatherName(userDetails.getFatherName());
        user.setGender(userDetails.getGender());
        user.setIsEnabled(true);
        user.setIsMobileVerified(userDetails.getIsMobileNumberVerified());
        user.setCurrentAccountAvailable(userDetails.getCurrentAccountAvailable());
        if(userDetails.getCompanyName()!=null){
            user.setCompanyName(userDetails.getCompanyName());
        }
    }
}
