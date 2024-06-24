package com.sr.capital.kyc.external.response.extraction;


import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.extraction.data.AadhaarExtractionResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AadhaarExtractionResponse extends KarzaBaseResponse<AadhaarExtractionResponseData> {

}
