package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.TenantBankDetails;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantBankResponseDto {

    private UUID id;

    Long userId;

    Long baseBankId;

    @NotEmpty(message = "accountNumber cannot be null")
    String accountNumber;

    String accountHolderName;

    String ifscCode;

    String accountStatementLink;

    private Boolean isAccountVerified ;

    public static TenantBankResponseDto mapResponse(TenantBankDetails tenantBankDetails){
        TenantBankResponseDto responseDto =new TenantBankResponseDto();
        responseDto.setId(tenantBankDetails.getId());
        responseDto.setAccountNumber(tenantBankDetails.getAccountNumber());
        responseDto.setBaseBankId(tenantBankDetails.getBaseBankId());
        responseDto.setIfscCode(tenantBankDetails.getIfscCode());
        responseDto.setAccountStatementLink(tenantBankDetails.getAccountStatementLink());
        responseDto.setIsAccountVerified(tenantBankDetails.getIsAccountVerified());
        return responseDto;
    }
}
