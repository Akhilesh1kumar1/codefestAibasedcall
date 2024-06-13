package com.sr.capital.kyc.external.request.verification.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GstVerificationData implements Serializable {

    private String gstin;

}
