package com.sr.capital.kyc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.extraction.data.DocumentExtractionData;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class VerifyGstOtpRequest {

    String requestId;

    String otp;


}
