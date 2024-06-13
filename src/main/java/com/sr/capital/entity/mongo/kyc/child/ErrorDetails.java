package com.sr.capital.entity.mongo.kyc.child;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails implements Serializable {

    @Field("error")
    private String error;

    @Field("message")
    private String message;

    public Boolean isNotNull(){
        return (StringUtils.hasLength(this.error) && StringUtils.hasLength(this.message));
    }
}
