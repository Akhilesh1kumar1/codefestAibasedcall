package com.sr.capital.dto.request;

import com.sr.capital.external.common.request.DocumentUploadRequestDto;
import com.sr.capital.helpers.enums.ProviderRequestTemplateType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Map;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
@Builder
@FieldNameConstants
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

    String sanctionCode;

    Map<String, Object> params;

    Object externalRequestBody;

    Class<?> responseClass;

    List<DocumentUploadRequestDto> documentUploadRequestDtos;

    @Builder.Default
    Boolean postSanction=false;

    @Data
    @FieldDefaults(level=AccessLevel.PRIVATE)
    @Builder
    public static class ValidateLoanData{

        String mobileNumber;

        String panNumber;
    }
}
