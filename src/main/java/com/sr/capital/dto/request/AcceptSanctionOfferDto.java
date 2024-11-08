package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcceptSanctionOfferDto {

    String sanctionCode;


    UUID loanApplicationId;

    String loanVendorName;

    Long loanVendorId;

    Boolean acceptOffer;

    String reason;
}
