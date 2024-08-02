package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.AccessType;

@Data
public class ItrAdditionalResponseData {



    @Data
    @Builder
    public static class AIS{
        Profile profile;
        ItrData data;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile{
        private String pan;
        private String aadharNumber;
        private String name;
        private String mobileNumber;
        private String emailAddress;
        private String address;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItrData{
        Object details;

        Object total;

        Object analysis;

        String financialYear;
    }

    
}
