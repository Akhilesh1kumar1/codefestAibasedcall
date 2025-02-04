package com.sr.capital.entity.mongo.los;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Document(collection = "los_user")
@Builder
@EqualsAndHashCode(callSuper = true)
@Getter
public class LosUserEntity extends BaseDoc {
    @Field("mobile")
    private String mobile;
    @Field("sr_company_id")
    private String srCompanyId;
    @Field("user_id")
    private String userId;
    @Field("first_name")
    private String firstName;
    @Field("last_name")
    private String lastName;
    @Field("email")
    private String email;
    @Field("type_of_entity")
    private String typeOfEntity;
    @Field("pan")
    private String pan;
    @Field("loan_amount")
    private BigDecimal loanAmount;
    @Field("is_mobile_verified")
    private Boolean isMobileVerified;
}
