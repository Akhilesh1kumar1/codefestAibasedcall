package com.sr.capital.entity.primary;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.entity.primary.interfaces.Auditable;
import com.sr.capital.listner.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners(AuditEntityListener.class)
@JsonIgnoreProperties({"auditData"})
public class UUIDBaseEntity implements Serializable, Auditable {

    @Id
    @GeneratedValue
    @Column(name="id", columnDefinition = "BINARY(16)", insertable = false, updatable = false, nullable = false)
    private UUID id;

    @Embedded
    private AuditData auditData;

    @Column(name = "is_enabled")
    private Boolean isEnabled = false;
}
