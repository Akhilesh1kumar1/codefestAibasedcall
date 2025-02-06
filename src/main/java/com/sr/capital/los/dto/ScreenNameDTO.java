package com.sr.capital.los.dto;

import com.sr.capital.los.utill.LosScreenStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScreenNameDTO {
    private String screenName;
    private Object personalDetails;
    private Object businessDetails;
    private Object documentUpload;
    private Object offerGeneration;
    private Object offerAcceptance;
    private Object basicDetails;

}