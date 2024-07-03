package com.sr.capital.kyc.external.response.extraction;

import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.extraction.data.GstDetailsByPanResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GstDetailsByPanResponse extends KarzaBaseResponse<List<GstDetailsByPanResponseData>> {
}
