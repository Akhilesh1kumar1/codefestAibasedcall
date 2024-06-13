package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.EnachLinkingEntity;
import com.sr.capital.entity.TenantBankDetails;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EnachLinkingResponseDto extends TenantBankResponseDto{


    public static EnachLinkingResponseDto mapResponse(EnachLinkingEntity tenantBankDetails){
        EnachLinkingResponseDto responseDto =new EnachLinkingResponseDto();
        responseDto.setId(tenantBankDetails.getId());
        return responseDto;
    }
}
