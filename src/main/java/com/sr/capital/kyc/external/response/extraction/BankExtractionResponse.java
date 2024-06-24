package com.sr.capital.kyc.external.response.extraction;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.extraction.data.BankExtractionResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankExtractionResponse extends KarzaBaseResponse<BankExtractionResponseData> {

}
