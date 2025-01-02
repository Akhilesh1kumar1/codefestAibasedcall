package com.sr.capital.external.truthscreen.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOfBusiness {

    @JsonProperty("address")
    private String address;
    @JsonProperty("contact_details")
    private String contactDetails;
    @JsonProperty("nature_of_business_activities")
    private String natureOfBusinessActivities;
    private String type;
}
