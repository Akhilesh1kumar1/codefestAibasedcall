package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateMobileResponse {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("exists")
    private Boolean exists;

    @JsonProperty("main_user")
    private Boolean mainUser;

    @JsonProperty("all_permission")
    private Boolean allPermission;

    @JsonProperty("user_id")
    private Integer userId;
}
