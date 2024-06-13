package com.sr.capital.entity.mongo.kyc;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDoc implements Serializable {

    @Id
    private String id;

    @Field("is_active")
    private Boolean isActive = Boolean.TRUE;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Field("last_modified_at")
    private LocalDateTime lastModifiedAt;

    @LastModifiedBy
    @Field("last_modified_by")
    private String lastModifiedBy;

}
