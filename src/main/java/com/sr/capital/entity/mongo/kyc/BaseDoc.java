package com.sr.capital.entity.mongo.kyc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String createdBy ="SYSTEM";;

    @CreatedDate
    @Field("created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Field("last_modified_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt =LocalDateTime.now();

    @LastModifiedBy
    @Field("last_modified_by")
    private String lastModifiedBy="SYSTEM";

}
