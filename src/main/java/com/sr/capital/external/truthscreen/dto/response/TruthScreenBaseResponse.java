package com.sr.capital.external.truthscreen.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TruthScreenBaseResponse<T> implements Serializable {

    private int status;
    private T msg;
    private String tsTransID;

}
