package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerDetailsResponse {
    @JsonProperty("download_url")
    String downLoadUrl;
}
