package com.sr.capital.external.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateLoanDetailsResponse {

    Boolean success;

    String message;

    List<LoanDetails> loanDetails;

    List<Errors> errors;


    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LoanDetails {
        String loanId;

        String clientLoanId;

        String status;

        String leadCode;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Errors {

        String code;

        String message;

        String stack;

        String isExisting;
    }

}
