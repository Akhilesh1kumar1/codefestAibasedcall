package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.FileDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DocUploadResponseDto<T> {


    String imageUrl;

    KycDocDetails<?> kycDocDetails;

}
