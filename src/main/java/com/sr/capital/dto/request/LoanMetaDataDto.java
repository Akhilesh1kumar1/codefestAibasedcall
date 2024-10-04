package com.sr.capital.dto.request;

import com.sr.capital.helpers.enums.ProviderRequestTemplateType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
@Builder
public class LoanMetaDataDto {

    String loanVendorName;

    Long loanVendorId;

    ProviderRequestTemplateType providerRequestTemplateType;

    @Builder.Default
    Boolean isCallClient=true;

    ValidateLoanData validateLoanData;

    CreateLeadRequestDto createLeadRequestDto;

    String leadId;

    String loanId;

    Map<String, Object> params;

    Object externalRequestBody;

    Class<?> responseClass;
    @Data
    @FieldDefaults(level=AccessLevel.PRIVATE)
    @Builder
    public static class ValidateLoanData{

        String mobileNumber;

        String panNumber;
    }
}
