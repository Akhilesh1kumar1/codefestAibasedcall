package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.CompanyType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class TenantDetails {

    Long capitalUserId;

    Long srUserId;

    String name;

    String srCompanyId;

    CompanyType companyType;

    Boolean privateLimited;

    String pan;

    String mobileNumber;

    AddressDto address;

    Boolean sellerRegisteredOnPlatform;

    String emailId;
}
