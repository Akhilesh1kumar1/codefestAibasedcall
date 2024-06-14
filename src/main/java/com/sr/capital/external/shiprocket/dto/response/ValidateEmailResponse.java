package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateEmailResponse implements Serializable {

    @JsonProperty("exists")
    private Boolean exists;

    @JsonProperty("main_user")
    private Boolean mainUser;

    @JsonProperty("all_permission")
    private Boolean allPermission;

    @JsonProperty("user_id")
    private Integer userId;
}
